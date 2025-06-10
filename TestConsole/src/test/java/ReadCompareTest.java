import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ReadCompareTest {
    @Mock
    Read read;

    @Mock
    Write write;


    @Test
    void readCompareTest
    doReturn("0x12345678").when(read).getReturn();


}