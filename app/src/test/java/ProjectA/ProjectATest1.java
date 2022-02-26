package ProjectA;

import org.junit.jupiter.api.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumingThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class ProjectATest1 {

    private static boolean allTestsPassed = true;
    private static boolean testingFrontBackArray = false, testingFrontBackList = true;
    private static FrontBackCappedListInterface<Integer> list;
    private int size;
    private String expected;
    private int temp;

    @BeforeEach
    public void beforeEach(){
        System.out.println("\nCHECKING SIZE\n");
        assertEquals(expected,list.toString(),"");
    }

    @AfterEach public void afterEach() {
        System.out.println("Expected: " + expected + "\n" +
                "Actual: "+ list.toString());
        assumingThat(list.size() == (temp),()->{
            System.out.println("\nCorrect size, Checking rest of List Attributes\n");
            assertEquals(expected,list.toString(),()->"\n==LIST ATTRIBUTES INCORRECT==");
        });
    }

    @Test
    public void runEmptyListTests() {
        list = new ArrayFrontBackCappedList<>(10);
        System.out.println("-----------------------------Testing an Empty list-----------------------------");

        assertFalse(list::isFull, () -> "Expected: False" + "\n Actual: " + list.isFull());
        temp = 0;
//        testSize(list, 0);
        assertTrue(list::isEmpty, () -> "Expected: True " + "\n Actual: " + list.isEmpty());
        expected = "size=0; capacity=10;	[]";
    }


    @RepeatedTest(value = 5, name = "{displayName} - {currentRepetition} / {totalRepetitions}")
    @DisplayName("Adding to back:")
    public void runAddToBackTests() {
        System.out.println("\n-----------------------------TESTING ADD TO BACK-----------------------------");

        testAdd(list, ProjectADriver.AddRemovePosition.BACK, new Integer[]{7}, true, "addBack to empty list");
        expected = "size=1; capacity=10;	[7]";
        testAdd(list, ProjectADriver.AddRemovePosition.BACK, new Integer[]{9, 5}, true, "addBack to non-empty list");

        assertFalse(list::isFull, () -> "Expected: False" + "\n Actual: " + list.isFull());
        assertFalse(list::isEmpty, () -> "Expected: True " + "\n Actual: " + list.isEmpty());

        assumingThat(list.toString().equals("size=3; capacity=10;	[7, 9, 5]"),()->{
            System.out.println("List contents match by string");
            assertEquals(3,list.size());
        });
    }

//    @TestFactory
    public <T> void testAdd(FrontBackCappedListInterface<T> list, ProjectADriver.AddRemovePosition positionToAdd, T[] valuesToAdd, boolean expectedResult, String testDescription) {
        System.out.println("\nTesting add method: trying to add " + Arrays.toString(valuesToAdd) + " to " + positionToAdd + " of list");
        System.out.println("List before adding: " + list);

        int beforeSize = list.size();
        for (T value : valuesToAdd) {
            boolean actualResult;
            if (positionToAdd == ProjectADriver.AddRemovePosition.FRONT) {
                actualResult = list.addFront(value);
            } else { // positionToAdd==Position.BACK
                actualResult = list.addBack(value);
            }
            if (actualResult != expectedResult) {
                allTestsPassed = false;
                System.out.println("**********TEST FAILED: incorrect return value when adding " + value + ".\ttest:" + testDescription);
                System.out.println("Expected return result: " + expectedResult + "\n  Actual return result: " + actualResult);
            }
        }
        if (expectedResult == false && beforeSize == list.size()) {
            System.out.println("List was full and add was unsuccessful, as expected.");
        }
        System.out.println("List after adding:  " + list);
        if (expectedResult == true) {
            int afterSize = list.size();
            int expectedAfterSize = beforeSize + valuesToAdd.length;
            if (expectedAfterSize != afterSize) {
                allTestsPassed = false;
                System.out.println("**********TEST FAILED: incorrect size after adding " + Arrays.toString(valuesToAdd) + ".\ttest:" + testDescription);
                System.out.println("\nExpected after size: " + expectedAfterSize + "\n  Actual after size: " + afterSize);
            } else { // expectedAfterSize == afterSize
                T expectedLastAddedValue = valuesToAdd[valuesToAdd.length - 1];
                T actualLastAddedValue;
                if (positionToAdd == ProjectADriver.AddRemovePosition.FRONT) {
                    actualLastAddedValue = list.getEntry(0);
                } else { // positionToAdd==Position.BACK
                    actualLastAddedValue = list.getEntry(list.size() - 1);
                }
                if (!expectedLastAddedValue.equals(actualLastAddedValue)) {
                    allTestsPassed = false;
                    System.out.println("**********TEST FAILED: incorrect list contents. Review the output below.\ttest:" + testDescription);
                }
            }
        }
    }

    public static enum AddRemovePosition {
        FRONT, BACK;

        public String toString() {
            return super.toString().toLowerCase();
        }
    }
    public static enum IndexPosition {
        FIRST, LAST;

        public String toString() {
            return super.toString().toLowerCase();
        }
    }
}
