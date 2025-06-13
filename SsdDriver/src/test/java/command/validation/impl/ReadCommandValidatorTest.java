package command.validation.impl;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ReadCommandValidatorTest {

    private final ReadCommandValidator validator = new ReadCommandValidator();

    @Test
    void 유효한_주소가_들어오면_검증에_성공한다() {
        String[] args = {"R", "10"};
        assertThat(validator.validate(args)).isTrue();
    }

    @Test
    void 주소가_범위를_벗어나면_검증에_실패한다() {
        String[] args = {"R", "999999"};
        assertThat(validator.validate(args)).isFalse();
    }

    @Test
    void 주소가_숫자가_아니면_검증에_실패한다() {
        String[] args = {"R", "ABCD"};
        assertThat(validator.validate(args)).isFalse();
    }

    @Test
    void 인자가_2개가_아니면_검증에_실패한다() {
        assertThat(validator.validate(new String[]{"R"})).isFalse();
        assertThat(validator.validate(new String[]{"R", "100", "EXTRA"})).isFalse();
    }

}