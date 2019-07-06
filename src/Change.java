import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class for the state of the change machine which contains the total dollar amount in the machine, amount
 * of each type of the bills in the machine and the bill type in the machine.
 *
 * This class also contains all the functions for the change machine such as logic for "put", "take", "show",
 * and "change".
 */
public class Change {

    /**
     * Total dollar amount hold in the change machine.
     */
    private int total;

    /**
     * HashMap of each of the dollar bill remains in the change machine.
     */
    private static final HashMap<Integer, Integer> changes = new HashMap<>();

    /**
     * HashMap of each fo the dollar bill type in the change machine.
     */
    private static final HashMap<Integer, Integer> billType = new HashMap<>();

    /**
     * Private sorry message.
     */
    private static final String SORRY_MESSAGE = "sorry";

    /**
     * Private wrong input message.
     */
    private static final String WRONG_INPUT_MESSAGE = "Please enter a number!";

    /**
     * Class constructor, takes in list of string of initial dollar bills in the change machine.
     *
     * @param args - array of strings that contains the inital dollar bills amount
     */
    public Change(String[] args) {
        this.setup();
        this.total = 0;
        for (int i = 1; i <= args.length; ++i) {
            try {
                changes.put(billType.get(i), Integer.valueOf(args[i - 1]));
                this.total += billType.get(i) * Integer.valueOf(args[i - 1]);
            } catch (NumberFormatException e) {
                System.out.println("Wrong command line argument, failed to initialize the program!");
                throw(e);
            }
        }
    }

    /**
     * Main logic for "show" function. String format each of the dollar bill type into single string.
     *
     * For future use case when we want to add additional bill type, we can add additional bill type into
     * the printf statement.
     */
    public void show() {
        System.out.printf("$%d %d %d %d %d %d%n", this.total, changes.get(20), changes.get(10), changes.get(5),
                changes.get(2), changes.get(1));
    }

    /**
     * Main logic for "put" and "take" function. Given the user entered parameters for the amount user wants to add
     * into the change machine. Additional parameter of add to identify we are executing "put" or "take".
     *
     * @param params - array of strings that contains the dollar bills amount to put/take from the change machine
     * @param add - boolean variable that determine either executing put or take operation
     */
    public void putAndTake(String[] params, boolean add) {
        if (!add) {
            try {
                negateValues(params);
            } catch (NumberFormatException e) {
                return;
            }
        }

        int[] values = new int[6];

        for (int i = 1; i < 6; ++i) {
            try {
                values[i] = Integer.valueOf(params[i]);
            } catch (NumberFormatException e) {
                System.out.println(WRONG_INPUT_MESSAGE);
                return;
            }
        }

        for (int i = 1; i < 6; ++i) {
            int bill = billType.get(i);

            if (changes.get(bill) + values[i] < 0) {
                System.out.printf("Cannot withdraw more than %d $%d bills, removed %d bills insread.%n",
                        changes.get(bill), bill, changes.get(bill));
                values[i] = -1 * changes.get(bill);
            }

            changes.put(bill, (changes.get(bill) + values[i]));
            total += bill * values[i];
        }

        this.show();
    }

    /**
     * Main logic for "change" function. Given the required change amount, function creates temporary memoization
     * array and execute calculateChange function to calculate the change required and return as String.
     *
     * @param amount - integer value of the required change amount
     */
    public void makeChange(int amount) {
        if (amount > total) {
            System.out.println(SORRY_MESSAGE);
            return;
        }

        ArrayList<ArrayList<ArrayList<Integer>>> bills = new ArrayList<>(amount + 1);

        System.out.println(calculateChange(amount, bills));
    }

    /**
     * Set up function to initialize the bill type of the change machine. Can add additional bill type for
     * future use case if we want to have additional bill type.
     */
    private void setup() {
        billType.put(1, 20);
        billType.put(2, 10);
        billType.put(3, 5);
        billType.put(4, 2);
        billType.put(5, 1);
    }

    /**
     * Utility funciton to negate all values of numbers in String format in a given array.
     *
     * @param params - String array of the number values that want to be negated
     */
    private void negateValues(String[] params) {
        for (int i = 1; i < 6; ++i) {
            int negative;
            try {
                negative = -1 * Integer.valueOf(params[i]);
            } catch (NumberFormatException e) {
                System.out.println(WRONG_INPUT_MESSAGE);
                throw(e);
            }
            params[i] = String.valueOf(negative);
        }
    }

    /**
     * Logic to execute dynamic programming solution to find out how to grant changes given a dollar amount.
     *
     * @param amount - integer value of the required change amount
     * @param bills - array of the temporary memoization array
     *
     * @return String of each type of the dollar bill returned as change
     */
    private String calculateChange(int amount, ArrayList<ArrayList<ArrayList<Integer>>> bills) {
        for (int i = 0; i <= billType.size() ; ++i) {
            for (int j = 0; j <= amount; ++j) {
                if (i == 0) {
                    // Initialize memoization with empty ArrayLists with size of the amount of change
                    ArrayList<ArrayList<Integer>> option = new ArrayList<>();
                    bills.add(option);
                } else if (j > 0) {
                    int bill = billType.get(billType.size()+1-i);

                    if (j == bill) {
                        // Case when current amount is the same of the bill
                        matchBill(i, j, bills);
                    } else if (j > bill) {
                        // Case when the current amount is bigger than the bill
                        amountBiggerThanBill(i, j, bill, bills);
                    } else {
                        // Case when the current amount is smaller than the bill
                        for (ArrayList<Integer> l : bills.get(j)) {
                            if (l.size() > 0) {
                                l.add(0, 0);
                            }
                        }
                    }
                }
            }
        }

        return resultString(amount, bills);
    }

    /**
     * Operation of updating the memoization array when current amount is the same of the bill. The function add a
     * new array with 1 type of the current bill and 0 types of the other bill onto the memoization array at index
     * of the current amount given by j.
     *
     * @param i - bill type index
     * @param j - dollar amount index
     * @param bills - memoization array
     */
    private void matchBill(int i, int j, ArrayList<ArrayList<ArrayList<Integer>>> bills) {
        ArrayList<Integer> newOption = new ArrayList<>();

        // Populate new ArrayList
        for (int index = 0; index < i; ++index) {
            if (index == 0) {
                newOption.add(1);
            } else {
                newOption.add(0);
            }
        }

        bills.get(j).add(newOption);
    }

    /**
     * Operation of updating the memoization array when current amount is larger than the bill. The function will
     * add 1 more bill into the change combo for current amount, given the combination that made up the previous
     * combo of (amount - bill).
     *
     * @param i - bill type index
     * @param j - dollar amount index
     * @param bill - current bill type
     * @param bills - memoization array
     */
    private void amountBiggerThanBill(int i, int j, int bill, ArrayList<ArrayList<ArrayList<Integer>>> bills) {
        int diff = j - bill;

        // Add 0 to previous combo that does not use the current bill type
        for (ArrayList<Integer> l : bills.get(j)) {
            if (l.size() > 0) {
                l.add(0, 0);
            }
        }

        // When a previous combo can satisfy the change given the current bill
        if (diff > 0 && bills.get(diff).size() > 0) {
            for (ArrayList<Integer> l : bills.get(diff)) {
                ArrayList<Integer> newOption = new ArrayList<>();

                if (l.size() < i) {
                    // If previous combo does not use the current bill type
                    newOption.add(1);
                    newOption.addAll(l);
                } else if (l.size() == i && l.get(0) + 1 <= changes.get(bill)) {
                    // If the previous combo uses the current bill type
                    newOption.add(1 + l.get(0));

                    for (int index = 1; index < l.size(); ++index) {
                        newOption.add(l.get(index));
                    }
                } else {
                    // Skip and do not add the new combo
                    continue;
                }

                bills.get(j).add(newOption);
            }
        }
    }

    /**
     * Function to format the result string given the current combos that satisfy the change requirement.
     *
     * @param amount - required change amount
     * @param bills - memoization array
     *
     * @return String of each type of the dollar bill returned as change
     */
    private String resultString(int amount, ArrayList<ArrayList<ArrayList<Integer>>> bills) {
        if (bills.get(amount) != null && bills.get(amount).size() > 0) {
            int min = Integer.MAX_VALUE;
            int index = -1;

            // Current implementation is to utilize the minimal amount of bill to be
            // removed from the machine to maintain most use out of the machine. Can
            // be changed to maximize the amount of bill given out to increase user
            // experience for better changes.
            for (int i = 0; i < bills.get(amount).size(); ++i) {
                int sum = 0;

                for (int value : bills.get(amount).get(i)) {
                    sum += value;
                }

                if (sum < min) {
                    index = i;
                }
            }

            return removeChangesAndGetString(bills.get(amount).get(index));
        } else {
            return SORRY_MESSAGE;
        }
    }

    /**
     * Function to remove the changes from the state of the machine and format the return string.
     *
     * @param list - list of the change combo given back to the user
     *
     * @return String of each type of the dollar bill returned as change
     */
    private String removeChangesAndGetString(ArrayList<Integer> list) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < list.size(); ++i) {
            int bill = billType.get(i+1);
            changes.replace(bill, changes.get(bill) - list.get(i));
            total -= list.get(i) * bill;
            result.append(list.get(i));
            result.append(" ");
        }

        return result.toString();
    }
}
