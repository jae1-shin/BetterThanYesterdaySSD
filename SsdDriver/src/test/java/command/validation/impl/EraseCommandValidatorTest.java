package command.validation.impl;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class EraseCommandValidatorTest {

    private final EraseCommandValidator validator = new EraseCommandValidator();

    @Test
    void 유효한_주소가_들어오면_검증에_성공한다() {
        String[] args = {"E", "10", "5"};
        assertThat(validator.validate(args)).isTrue();
    }

    @Test
    void 주소가_범위를_벗어나면_검증에_실패한다() {
        String[] args = {"E", "105", "3"};
        assertThat(validator.validate(args)).isFalse();
    }

    @Test
    void 삭제_범위가_벗어나면_검증에_실패한다() {
        String[] args = {"E", "99", "3"};
        assertThat(validator.validate(args)).isFalse();
    }

    @Test
    void 주소가_숫자가_아니면_검증에_실패한다() {
        String[] args = {"E", "XX12", "5"};
        assertThat(validator.validate(args)).isFalse();
    }

    @Test
    void 인자가_3개가_아니면_검증에_실패한다() {
        assertThat(validator.validate(new String[]{"E"})).isFalse();
        assertThat(validator.validate(new String[]{"E", "100"})).isFalse();
        assertThat(validator.validate(new String[]{"E", "100", "3", "33"})).isFalse();
    }

}