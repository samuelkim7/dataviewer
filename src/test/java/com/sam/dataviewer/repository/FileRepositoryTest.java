package com.sam.dataviewer.repository;

import com.sam.dataviewer.domain.Dashboard;
import com.sam.dataviewer.domain.File;
import com.sam.dataviewer.domain.Member;
import com.sam.dataviewer.domain.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.BDDAssertions.then;

@DataJpaTest(showSql = false)
@Transactional
class FileRepositoryTest {

    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    @DisplayName("원래 파일명으로 조회")
    public void findByOriginalFileNameTest() throws Exception {
        //given
        Member member = getMember("kim");
        testEntityManager.persist(member);
        Order order = getOrder(member, "order");
        testEntityManager.persist(order);
        File file1 = getFile(order, "file1");
        File file2 = getFile(order, "file2");
        testEntityManager.persist(file1);
        testEntityManager.persist(file2);
        testEntityManager.flush();
        testEntityManager.clear();

        //when
        File fileFound = fileRepository.findByOriginalFileName("file1");

        //then
        then(fileFound.getFileName()).isEqualTo("file");
        then(fileFound.getOriginalFileName()).isEqualTo("file1");
    }

    @Test
    @DisplayName("주문으로 조회 (id로 내림차순 정렬)")
    public void findByOrderOrderByIdDescTest() throws Exception {
        //given
        Member member = getMember("kim");
        testEntityManager.persist(member);
        Order order1 = getOrder(member, "order1");
        Order order2 = getOrder(member, "order2");
        testEntityManager.persist(order1);
        testEntityManager.persist(order2);

        IntStream.range(0, 5).forEach(i -> {
            File file = getFile(order1, "file" + (i+1));

            testEntityManager.persist(file);
            testEntityManager.flush();
            testEntityManager.clear();
        });

        IntStream.range(0, 5).forEach(i -> {
            File file = getFile(order2, "file" + (i+1));

            testEntityManager.persist(file);
            testEntityManager.flush();
            testEntityManager.clear();
        });

        //when
        List<File> files = fileRepository.findByOrderOrderByIdDesc(order1);

        //then
        then(files).hasSize(5);
        then(files.get(0).getOrder().getTitle()).isEqualTo("order1");
        then(files.get(0).getOriginalFileName()).isEqualTo("file5");
        then(files.get(4).getOriginalFileName()).isEqualTo("file1");
    }

    private File getFile(Order order, String originalFileName) {
        return File.createFile(order, originalFileName, "file", null, null);
    }


    private Member getMember(String username) {
        return Member.createMember(
                username, null, null,
                null, null, null, null);
    }

    private Order getOrder(Member member, String title) {
        return Order.createOrder(member, title, "content");
    }


}