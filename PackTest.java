import static org.junit.jupiter.api.Assertions.fail;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import cards.InvalidPackException;
import cards.Pack;

public class PackTest {
    private Pack pack;
    @BeforeEach
    public void setUp() {
        pack = new Pack(4);
        System.out.println("pack");
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
        try {
            pack.loadPack("thisPackDoesNotExist.txt");
            fail("Exception not thrown");
        }
        catch(FileNotFoundException e){

        } catch (InvalidPackException e) {
            fail("Incorrect exception thrown");
        }
    }

}
