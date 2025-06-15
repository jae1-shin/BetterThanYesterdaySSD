package command.validation.impl;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class FlushCommandValidatorTest {

    private final FlushCommandValidator validator = new FlushCommandValidator();

    @Test
    void 유효한_인자가_들어오면_검증에_성공한다() {
        assertThat(validator.validate(new String[]{"F"})).isTrue();
    }

    @Test
    void 유효하지않은_인자가_들어오면_검증에_실패한다() {
        assertThat(validator.validate(new String[]{"R"})).isFalse();
        assertThat(validator.validate(new String[]{"W"})).isFalse();
        assertThat(validator.validate(new String[]{"E"})).isFalse();
        assertThat(validator.validate(new String[]{"X"})).isFalse();
    }

    @Test
    void 인자가_1개가_아니면_검증에_실패한다() {
        assertThat(validator.validate(new String[]{"F", "10"})).isFalse();
        assertThat(validator.validate(new String[]{"F", "10", "0xABCDABCD"})).isFalse();
    }


}