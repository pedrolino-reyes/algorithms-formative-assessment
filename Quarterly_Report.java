import java.text.NumberFormat;
import java.util.*;

public class Quarterly_Report {
    // This is the main class used for running the application
    // Instantiate objects for each department
    static Department electrical = new Department("Electrical");
    static Department kitchen = new Department("Kitchen");
    static Department bathroom = new Department("Bathroom");
    static Department softFurnishing = new Department("Soft Furnishing");
    static Department accessories = new Department("Accessories");
    // Create a collection to hold the departments
    static Department[] departments = new Department[]{electrical, kitchen, bathroom, softFurnishing, accessories};

    public static void main(String[] args) {
        // We're only going to use hardcoded demo sales data for this application.
        // This data will be used to generate the reports. The app could be modified
        // to read data from a different source, but for now we just use the hardcoded data.
        addDemoSalesData();

        // Create an array of reports that our app can generate
        // The reports share the same public interface, which means we can update our main app easily by
        // just adding or removing report objects from this array.
        Report[] reports = new Report[]{
            new SalesPerQuarterReport(departments),
            new BestAndWorstDeptPerQuarterReport(departments),
            new MostEffectiveMonthPerQuarterReport(departments)
        };
        // Display the welcome screen and the reports menu
        welcomeScreen();
        reportsMenu(reports);
    }

    private static void welcomeScreen() {
         /*
            This method is used to display the main screen

            There are three reports available:
                1.	The total sales for each department per quarter i.e. “2nd Quarter totals: Electrical, £208,000”
                2.	The name of the best and worst preforming department per quarter, with its respective monthly sales
                    i.e. “2nd Quarter best: Electrical, £67,000, £63,000, £78,000” for the second quarter.
                3.	The most effective month for each department (within a quarter) and its sales for that month,
                    i.e. “3rd Quarter: Kitchen, September, £72,000; Electrical, August £104,000”
         */
        System.out.println("************    ******    ****         ************ ************");
        System.out.println("************   ********   ****         ************ ************");
        System.out.println("****          **********  ****         ****         ****        ");
        System.out.println("************ ****    **** ****         ************ ************");
        System.out.println("************ ************ ****         ************ ************");
        System.out.println("       ***** ************ ************ ****                *****");
        System.out.println("************ ****    **** ************ ************ ************");
        System.out.println("************ ****    **** ************ ************ ************");
        System.out.println("\n\n");
        System.out.println("***********  ************ ***********    ********   ***********  ************ ************");
        System.out.println("***********  ************ ************  **********  ***********  ************ ************");
        System.out.println("****    ***  ****         ***      *** ****    **** ****    ***  ************ ****        ");
        System.out.println("*********    ************ ************ ***      *** *********        ****     ************");
        System.out.println("*********    ************ ***********  ***      *** *********        ****     ************");
        System.out.println("****  ****   ****         ****         ****    **** ****  ****       ****            *****");
        System.out.println("****   ****  ************ ****          **********  ****   ****      ****     ************");
        System.out.println("****    **** ************ ****           ********   ****    ****     ****     ************");
        System.out.println("\n\n");
        System.out.println("Welcome to the Quarterly Sales Reports Application");
        System.out.println("-------------------------------------------\n");
    }

    private static void reportsMenu(Report[] reports) {
        /*
            This method is used to display the reports menu, which should be displayed on the main welcome
            screen, and after each report is displayed.
         */
        System.out.println("Please select a report to view:");
        for (int i = 0; i < reports.length; i++) {
            System.out.println("\t" + (i + 1) + ". " + reports[i].getName());
        }
        System.out.println("\t99. Exit the program\n");

        Scanner scanner = new Scanner(System.in);
        int reportChoice = 0;
        try {
            reportChoice = scanner.nextInt();
        } catch (InputMismatchException e) {
            scanner.next(); // This clears the invalid input
        }

        while (reportChoice < 1 || reportChoice > reports.length && reportChoice != 99) {
            System.out.println("Invalid choice. Please select a number between 1 and " + reports.length + ", or enter 99 to exit the program");
            reportsMenu(reports);
        }

        if (reportChoice == 99) {
            exitProgram();
        } else {
            System.out.println(reports[reportChoice - 1].getName() + " report");
            System.out.println("-------------------------------------------");
            System.out.println("\n" + reports[reportChoice - 1].getDescription() + "\n");
            reports[reportChoice - 1].generateReport();
            reportsMenu(reports);
        }
    }


    private static void exitProgram() {
        System.out.println("Exiting the program...");
        System.exit(0);
    }

    private static void addDemoSalesData() {
        // Add the demo sales data for each department
        // Our data array has 12 elements, one for each month of the year. Where there is no sales data
        // available, we use 0.
        addSalesData(electrical, new int[]{0,0,0, 67, 63, 78, 78, 104, 103, 0,0,0});
        addSalesData(kitchen, new int[]{0,0,0, 65, 67, 56, 45, 56, 72, 0,0,0});
        addSalesData(bathroom, new int[]{0,0,0, 63, 63, 65, 71, 73, 69, 0,0,0});
        addSalesData(softFurnishing, new int[]{0,0,0, 18, 24, 22, 19, 17, 16, 0,0,0});
        addSalesData(accessories, new int[]{0,0,0, 16, 23, 21, 19, 20, 19, 0,0,0});
    }

    private static void addSalesData(Department department, int[] salesData) {
        // This method is used to add the sales data for a department for a whole year
        for (int i = 0; i < salesData.length; i++) {
            department.addMonthlySales(i, salesData[i]);
        }
    }

}

class Month {
    /*
        This class is used to convert between a number and the name of the month, in an array where 0 is January
        and 11 is December
     */
    public static String getMonthName(int month) {
        // This method is used to get the name of the month
        String[] months = new String[]{
                "January", "February", "March",
                "April", "May", "June",
                "July", "August", "September",
                "October", "November", "December"
        };
        return months[month];
    }
}

class Currency {
    /*
        This class is used to format the currency in the application. Sales figures are logged in 1,000s
        of pounds, and stored as integers. When we want to display sales figures, we want to convert these
        integers into formatted currency strings.
     */
    public static String formatSalesValue(int value) {
        Double doubleValue = (double) value * 1000;
        Locale locale = new Locale("en", "GB");
        NumberFormat nf = NumberFormat.getCurrencyInstance(locale);
        return nf.format(doubleValue).replace(".00", "");
    }
}


class Department {
    /*
        This is class is used to instantiate an object for each department in the quarterly report
     */
    int[] annualSales = new int[12];
    String name;

    public Department(String name) {
        // This is the custom constructor for the Department class - when instantiating a new object,
        // the name of the department is required
        this.name = name;
    }

    public String getName() {
        // Accessor method to get the name of the department
        return this.name;
    }

    public void addMonthlySales(int month, int sales) {
        // This method is used to add the sales figures for each month
        this.annualSales[month] = sales;
    }

    public int getQuarterlySales(int quarter) {
        // This method is used to get the total quarterly sales for the department
        int quarterlySales = 0;
        for (int i = 0; i < 3; i++) {
            quarterlySales += this.annualSales[(quarter - 1) * 3 + i];
        }
        return quarterlySales;
    }

}


abstract class Report {
    /*
        This is the abstract class for the reports. It contains the abstract method generateReport(),
        which is implemented in the DepartmentReports class.
     */
    String name;
    String description;
    Department[] departments;

    public Report(String name, String description, Department[] departments) {
        this.name = name;
        this.description = description;
        this.departments = departments;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    // The generateReport method needs to be implemented by each subclass
    public abstract void generateReport();

}

class SalesPerQuarterReport extends Report {
    static String name = "Sales per quarter";
    static String description = "The total sales for each department per quarter";

    public SalesPerQuarterReport(Department[] departments) {
        super(name, description, departments);
    }

    @Override
    public void generateReport() {
            for (int q = 1; q <= 4; q++) {
                System.out.println("Quarter " + q + " sales: ");
                // check that data is available this quarter for at least one of the departments
                boolean quarterlyDataAvailable = false;
                for (Department department : departments) {
                    if (department.getQuarterlySales(q) > 0) {
                        quarterlyDataAvailable = true;
                        break;
                    }
                }
                if (quarterlyDataAvailable) {
                    for (Department dept: this.departments) {
                        System.out.println("\t" + dept.getName() + ": "  + Currency.formatSalesValue(dept.getQuarterlySales(q)));
                    }
                } else {
                    System.out.println("\tNo sales data available for any department");
                }
            System.out.println();
        }
    }
}

class BestAndWorstDeptPerQuarterReport extends Report {
    static String name = "Best and worst performing department per quarter";
    static String description = "The name of the best and worst preforming department per quarter, with their respective monthly sales";

    public BestAndWorstDeptPerQuarterReport(Department[] departments) {
        super(name, description, departments);
    }

    @Override
    public void generateReport() {
        for (int q = 1; q <= 4; q++) {
            // Sort the departments by quarterly sales
            int finalQ = q;
            Arrays.sort(departments, (d1, d2) -> Integer.compare(d2.getQuarterlySales(finalQ), d1.getQuarterlySales(finalQ)));
            if (departments[departments.length - 1].getQuarterlySales(q) > 0) {
                displayBestWorstDepartment(true, finalQ);
                displayBestWorstDepartment(false, finalQ);
            } else {
                System.out.println("Quarter " + finalQ);
                System.out.println("\tNo sales data available for quarter " + finalQ);
                System.out.println();
            }
        }
    }

    private void displayBestWorstDepartment(boolean best, int q) {
        Department dept;
        System.out.print("Quarter " + q);
        if (best) {
            dept = departments[0];
            System.out.println(" best: " + dept.getName());
        } else {
            dept = departments[departments.length - 1];
            System.out.println(" worst: " + dept.getName());
        }
        displayMonthlyDeptFigures(dept, q);
        System.out.println();
    }

    private void displayMonthlyDeptFigures(Department dept, int q) {
        for (int i = 0; i < 3; i++) {
            System.out.println("\t" + Month.getMonthName((q * 3) - 3 + i) + ": " + Currency.formatSalesValue(dept.annualSales[(q * 3) - 3 + i]));
        }
    }

}

class MostEffectiveMonthPerQuarterReport extends Report {
    static String name = "Most effective month for each department within a quarter";
    static String description = "The most effective month for each department (within a quarter), with its sales for that month";

    public MostEffectiveMonthPerQuarterReport(Department[] departments) {
        super(name, description, departments);
    }

    @Override
    public void generateReport() {
        for (int q = 1; q <= 4; q++) {
            System.out.println("Quarter " + q + ": ");

            // check that data is available this quarter for at least one of the departments
            boolean quarterlyDataAvailable = false;
            for (Department department : departments) {
                if (department.getQuarterlySales(q) > 0) {
                    quarterlyDataAvailable = true;
                    break;
                }
            }

            if (quarterlyDataAvailable) {
                for (Department dept : departments) {
                    String[] bestMonth = getMostEffectiveMonthInQuarter(dept, q);
                    System.out.println("\t" + dept.getName() + ", " + bestMonth[0] + ", " + bestMonth[1]);
                }
            } else {
                System.out.println("\tNo sales data available for quarter " + q);
            }
            System.out.println();
        }
    }

    private String[] getMostEffectiveMonthInQuarter(Department dept, int quarter) {
        /*
         * This method is used to get the most effective month - the month with the most sales -
         * in a given quarter. It returns an array of two integers: the number of the month, and
         * the total value of sales for that month.
         */
        int bestSales = 0;
        int bestMonth = 0;
        for (int i = 0; i < 3; i++) {
            int month = (quarter - 1) * 3 + i;
            if (dept.annualSales[month] > bestSales) {
                bestSales = dept.annualSales[month];
                bestMonth = month;
            }
        }
        return new String[]{Month.getMonthName(bestMonth), Currency.formatSalesValue(bestSales)};
    }

}
