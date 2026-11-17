package mn.edu.must.sqat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GcdTest {

    @Test
    @DisplayName("T1 gcd(7,0): zero iterations; covers pairs 2 (x13-20) and 6 (y13-15F)")
    void gcd_7_0_skipsLoop() {
        assertEquals(7, DataFlowTargets.gcd(7, 0));
    }

    @Test
    @DisplayName("T2 gcd(12,8): two iterations; covers pairs 1,3,4,5,7-13")
    void gcd_12_8_twoIterations() {
        assertEquals(4, DataFlowTargets.gcd(12, 8));
    }
}
