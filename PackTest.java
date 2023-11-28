import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import cards.Pack;

public class PackTest {
    Pack pack;
    @BeforeEach
    public void setup() {
        pack = new Pack(4);
    }
    @Test
    @DisplayName("Ensure pack is loaded correctly when given a valid file.")
    public void testLoadPack_ValidFile() {

    }
    @Test
    @DisplayName("")
    public void testLoadPack_NegativeIntegers() {

    }
    @Test
    @DisplayName("")
    public void testLoadPack_IncorrectNumberOfRows() {

    }
    @Test
    @DisplayName("")
    public void testLoadPack_FileDoesNotExist() {
        
    }

}
