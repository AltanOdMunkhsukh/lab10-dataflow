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

    @Test
    @DisplayName("BT3 null attendance -> exception")
    void bonus_nullAttendance_throws() {
        assertThrows(IllegalArgumentException.class, () -> DataFlowTargets.bonus(null, 3));
    }

    @Test
    @DisplayName("BT4 negative threshold -> exception")
    void bonus_negativeThreshold_throws() {
        assertThrows(IllegalArgumentException.class, () -> DataFlowTargets.bonus(new int[]{1}, -1));
    }

    @Test
    @DisplayName("BT5 empty attendance, threshold 0 -> 5")
    void bonus_emptyThresholdZero_is5() {
        assertEquals(5, DataFlowTargets.bonus(new int[]{}, 0));
    }

    @Test
    @DisplayName("BT6 all absent -> 0")
    void bonus_allAbsent_is0() {
        assertEquals(0, DataFlowTargets.bonus(new int[]{0, 0}, 1));
    }

    @Test
    @DisplayName("BT7 streak 5 but present below threshold -> 3")
    void bonus_streakBelowThreshold_is3() {
        assertEquals(3, DataFlowTargets.bonus(new int[]{1, 1, 1, 1, 1}, 6));
    }
}
