package command.validation.impl;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class WriteCommandValidatorTest {
    private final WriteCommandValidator validator = new WriteCommandValidator();

    @Test
    void 유효한_입력일_경우_검증에_성공한다() {
        String[] args = {"W", "5", "0xA1BBBBBB"};
        assertThat(validator.validate(args)).isTrue();
    }

    @Test
    void 주소값이_유효하지_않으면_검증에_실패한다() {
        String[] args = {"W", "ZZZZ", "ABCD"};
        assertThat(validator.validate(args)).isFalse();
    }

    @Test
    void 주소값의_범위가_유효하지_않으면_검증에_실패한다() {
        String[] args = {"W", "103", "ABCD"};
        assertThat(validator.validate(args)).isFalse();
    }

    @Test
    void 데이터가_없으면_검증에_실패한다() {
        String[] args = {"W", "4"};
        assertThat(validator.validate(args)).isFalse();
    }

    @Test
    void 데이터형식이_잘못되었을_경우_검증에_실패한다() {
        String[] args = {"W", "4", "!!??"};
        assertThat(validator.validate(args)).isFalse();
    }

    @Test
    void 인자가_너무_적으면_검증에_실패한다() {
        String[] args = {"W"};
        assertThat(validator.validate(args)).isFalse();
    }
}