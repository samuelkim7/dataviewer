package com.sam.dataviewer.adminController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sam.dataviewer.domain.Dashboard;
import com.sam.dataviewer.domain.Figure;
import com.sam.dataviewer.domain.Member;
import com.sam.dataviewer.domain.Order;
import com.sam.dataviewer.dto.DashboardDto;
import com.sam.dataviewer.dto.FigureDto;
import com.sam.dataviewer.dto.MemberDto;
import com.sam.dataviewer.dto.OrderDto;
import com.sam.dataviewer.repository.DashboardRepository;
import com.sam.dataviewer.repository.FigureRepository;
import com.sam.dataviewer.repository.OrderRepository;
import com.sam.dataviewer.service.DashboardService;
import com.sam.dataviewer.service.FigureService;
import com.sam.dataviewer.service.MemberService;
import com.sam.dataviewer.service.OrderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@Transactional
@WithMockUser(roles = "ADMIN")
class AdminFigureControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    private FigureService figureService;
    @Autowired
    private FigureRepository figureRepository;
    @Autowired
    private DashboardService dashboardService;
    @Autowired
    private DashboardRepository dashboardRepository;
    @Autowired
    private MemberService memberService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;

    @Test
    @DisplayName("figure 생성 폼")
    public void createFormTest() throws Exception {
        Long orderId = getOrder();
        getDashboard(orderId);
        MockHttpServletResponse response =
                mockMvc.perform(get("/admin/figure/new"))
                        .andExpect(status().isOk())
                        .andExpect(view().name("admin/figure/createFigureForm"))
                        .andExpect(model().attributeExists("dashboardDtos", "figureDto"))
                        .andReturn().getResponse();

        then(response.getContentAsString()).contains("대시보드");
    }

    @Test
    @DisplayName("figure 생성")
    public void createFigureTest() throws Exception {
        Long orderId = getOrder();
        Long dashboardId = getDashboard(orderId);
        FigureDto dto = new FigureDto(
                null, "figure", "설명",
                null, null, null,
                null, null, null
        );
        mockMvc.perform(post("/admin/figure/new").with(csrf())
                .param("dashboardId", dashboardId.toString())
                .param("title", dto.getTitle())
                .param("description", dto.getDescription()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/figures"));

        Dashboard dashboard = dashboardRepository.getOne(dashboardId);
        List<Figure> figures = dashboard.getFigures();
        then(figures.get(0).getTitle()).isEqualTo("figure");
        then(figures.get(0).getDescription()).isEqualTo("설명");
        then(figures.get(0).getDashboard()).isEqualTo(dashboard);
    }

    @Test
    @DisplayName("figure 리스트 보기")
    public void figureListTest() throws Exception {
        Long orderId = getOrder();
        Long dashboardId = getDashboard(orderId);
        getFigure(dashboardId, "figure1");
        getFigure(dashboardId, "figure2");
        MockHttpServletResponse response = mockMvc.perform(get("/admin/figures"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/figure/figureList"))
                .andExpect(model().attributeExists("figureDtos"))
                .andReturn().getResponse();

        then(response.getContentAsString())
                .contains("대시보드", "figure1", "figure2");
    }

    @Test
    @DisplayName("figure 상세보기")
    public void figureDetailTest() throws Exception {
        Long orderId = getOrder();
        Long dashboardId = getDashboard(orderId);
        Figure figure = getFigure(dashboardId, "figure");
        MockHttpServletResponse response = mockMvc.perform(
                get("/admin/figure/figureDetail/{id}", figure.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/figure/figureDetail"))
                .andExpect(model().attributeExists("figureDto"))
                .andReturn().getResponse();

        then(response.getContentAsString())
                .contains("대시보드", "figure");
    }

    @Test
    @DisplayName("figureView")
    public void figureViewTest() throws Exception {
        Long orderId = getOrder();
        Long dashboardId = getDashboard(orderId);
        Figure figure = getFigure(dashboardId, "figure");
        mockMvc.perform(get("/admin/figure/figureView/{id}", figure.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/figure/figureView"))
                .andExpect(model().attributeExists("figureDto"));
    }

    @Test
    @DisplayName("figure 수정하기")
    public void updateFigureTest() throws Exception {
        Long orderId = getOrder();
        Long dashboardId = getDashboard(orderId);
        Figure figure = getFigure(dashboardId, "figure");
        FigureDto dto = new FigureDto(
                figure.getId(), "figure1", "설명1",
                null, null, null,
                null, null, null
        );
        mockMvc.perform(post("/admin/figure/update").with(csrf())
                .param("id", dto.getId().toString())
                .param("title", dto.getTitle())
                .param("description", dto.getDescription()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/figures"));

        then(figure.getTitle()).isEqualTo("figure1");
        then(figure.getDescription()).isEqualTo("설명1");
    }

    @Test
    @DisplayName("파일 다운로드")
    public void downloadFileTest() throws Exception {
        MockMultipartFile file = getMultipartFile("sample.txt");
        Path path = Path.of("C:/spring/dataviewer_files/" + file.getOriginalFilename());
        Files.write(path, file.getBytes());

        MockHttpServletResponse response = mockMvc.perform(
                get("/admin/figure/downloadFile/{fileName}"
                        , file.getOriginalFilename()))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        then(response.getContentType()).isEqualTo("application/octet-stream");
        then(response.getContentAsString()).isEqualTo("sample.txt");

        //파일 삭제
        Files.delete(path);
    }

    @Test
    @DisplayName("figure 삭제하기")
    public void deleteFigureTest() throws Exception {
        Long orderId = getOrder();
        Long dashboardId = getDashboard(orderId);
        Figure figure = getFigure(dashboardId, "figure");
        Long id = figure.getId();
        mockMvc.perform(get("/admin/figure/delete/{id}", id))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/figures"));

        then(figureRepository.findById(id)).isEmpty();
    }

    private MockMultipartFile getMultipartFile(String originalFileName) {
        return new MockMultipartFile(
                "data", originalFileName,
                "text/plain", originalFileName.getBytes());
    }

    private Long getOrder() {
        MemberDto memberDto = new MemberDto(
                "kim", "1234", "Sam", "abc@gmail.com",
                "01011110000", null, null
        );
        Member member = memberService.join(memberDto);

        OrderDto orderDto = new OrderDto(
                null, "의뢰", "내용",
                null, null
        );
        return orderService.order(member.getUsername(), orderDto);
    }

    private Long getDashboard(Long orderId) {
        DashboardDto dashboardDto = new DashboardDto(
                null, "대시보드", "내용",
                null, null
        );
        return dashboardService.create(orderId, dashboardDto);
    }

    private Figure getFigure(Long dashboardId, String title) {
        FigureDto figureDto = new FigureDto(
                null, title, "설명",
                null, null, null,
                null, null, null
        );
        Long id = figureService.create(
                dashboardId, figureDto, null, null
        );
        return figureRepository.getOne(id);
    }

}