package com.sam.dataviewer.domain;

import com.sam.dataviewer.dto.FileDto;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;

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
        then(order).isEqualTo(file.getOrder());
        then(file).isEqualTo(order.getFiles().get(0));
        then(originalFileName).isEqualTo(file.getOriginalFileName());
        then(fileSize).isEqualTo(file.getFileSize());
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
        then(file.getOriginalFileName()).isEqualTo(dto.getOriginalFileName());
        then(file.getFileName()).isEqualTo(dto.getFileName());
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