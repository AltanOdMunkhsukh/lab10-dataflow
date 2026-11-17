package mn.edu.must.sqat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BonusTest {

    @Test
    @DisplayName("BT1 streak 5 with a break, threshold met -> 8")
    void bonus_streakWithBreak_is8() {
        assertEquals(8, DataFlowTargets.bonus(new int[]{1, 1, 1, 1, 1, 0, 1}, 3));
    }

    @Test
    @DisplayName("BT2 present below threshold, short streak -> 0")
    void bonus_belowThreshold_is0() {
        assertEquals(0, DataFlowTargets.bonus(new int[]{1}, 2));
    }
}
