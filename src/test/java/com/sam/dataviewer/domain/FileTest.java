package com.sam.dataviewer.domain;

import com.sam.dataviewer.dto.FileDto;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class FileTest {

    @Test
    public void createFile() throws Exception {
        //given
        Member member = getMember();
        Order order = getOrder(member);

        String originalFileName = "data";
        Long fileSize = 500L;

        //when
        File file = File.createFile(
                order, originalFileName,
                null, null, fileSize
        );

        //then
        assertThat(order).isEqualTo(file.getOrder());
        assertThat(file).isEqualTo(order.getFiles().get(0));
        assertThat(originalFileName).isEqualTo(file.getOriginalFileName());
        assertThat(fileSize).isEqualTo(file.getFileSize());
    }

    @Test
    public void toDto() throws Exception {
        //given
        Member member = getMember();
        Order order = getOrder(member);
        File file = File.createFile(
                order, "data",
                "data1", null, null
        );

        //when
        FileDto dto = file.toDto();

        //then
        assertThat(file.getOriginalFileName()).isEqualTo(dto.getOriginalFileName());
        assertThat(file.getFileName()).isEqualTo(dto.getFileName());
    }

    private Member getMember() {
        return Member.createMember(
                "kim", null,
                null, null, null,
                null, null
        );
    }

    private Order getOrder(Member member) {
        return Order.createOrder(member, "의뢰", "내용");
    }

}