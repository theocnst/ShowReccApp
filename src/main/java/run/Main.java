package run;

import enums.CategoryEnum;
import enums.RoleEnum;
import proiect.Platform;
import proiect.Show;
import proiect.User;

import java.time.LocalDate;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        Platform platform = new Platform();
        User currentUser = new User();
        boolean online = false;

        //the log in/register menu loop
        int loopCase = -1;
        Scanner scanner = new Scanner(System.in);

        //read a string and try catch the exception if it is not an integer


        while (loopCase != 0) {
            showLoginRegister();
            loopCase = scanner.nextInt();
            switch (loopCase) {
                case 0:
                    System.out.println("Exiting...");
                    break;
                case 1:
                    System.out.println("Enter username:");
                    scanner.nextLine();
                    currentUser.setUsername(scanner.nextLine());

                    System.out.println("Enter password:");
                    currentUser.setPassword(scanner.nextLine());

                    currentUser.setRole(RoleEnum.USER);

                    int code = platform.loginUser(currentUser.getUsername(), currentUser.getPassword());
                    if (code == 1) {
                        currentUser.setRole(RoleEnum.ADMIN);
                        online = true;
                        loopCase = 0;
                    }
                    if (code == 2) {
                        online = true;
                        loopCase = 0;
                    }
                    if (code == 0) {
                        continue;
                    }
                    break;
                case 2:
                    System.out.println("Enter username:");
                    scanner.nextLine();
                    currentUser.setUsername(scanner.nextLine());

                    boolean exists = platform.getUsers().stream().anyMatch((User u) -> currentUser.getUsername().equals(u.getUsername()));
                    if (exists) {
                        System.out.println("Username already exists");
                        break;
                    } else {
                        System.out.println("Enter password:");
                        currentUser.setPassword(scanner.nextLine());

                        currentUser.setRole(RoleEnum.USER);

                        platform.registerUser(currentUser);
                        online = true;
                    }
                    break;
                default:
                    break;
            }
            if (loopCase != 0) {
                try {
                    System.in.read();
                } catch (Exception e) {
                    System.out.println("Err");
                }
            }
        }

        //The user/admin menu loop
        String showTitle = "";
        loopCase = -1;
        while (loopCase != 0) {
            if (currentUser.getRole().equals(RoleEnum.ADMIN) && online) {
                showAdminMenu();
            } else if (online) {
                showUserMenu();
            }
            loopCase = scanner.nextInt();

            switch (loopCase) {
                case 0:
                    System.out.println("Exiting...");
                    break;
                case 1:
                    //view shows
                    platform.displayShuffledShows();
                    break;
                case 2:
                    //like show
                    System.out.println("What show do you like ?");
                    scanner.nextLine();
                    showTitle = scanner.nextLine();
                    platform.feedByShow(showTitle);
                    break;
                case 3:
                    //display personal feed by no. likes
                    platform.feedByLikes(showTitle);
                    break;
                case 4:
                    //display personal feed by release date
                    platform.feedByRelease(showTitle);
                    break;
                case 5:
                    if (currentUser.getRole().equals(RoleEnum.ADMIN)) {
                        //add show
                        Show currentShow = new Show();
                        System.out.println("Enter title of show:");
                        scanner.nextLine();
                        currentShow.setTitle(scanner.nextLine());

                        System.out.println("Choose category:");
                        showCategory();

                        int categoryCase = scanner.nextInt();
                        switch (categoryCase) {
                            case 1:
                                currentShow.setCategory(CategoryEnum.ANIME);
                                break;
                            case 2:
                                currentShow.setCategory(CategoryEnum.FANTASY);
                                break;
                            case 3:
                                currentShow.setCategory(CategoryEnum.CARTOON);
                                break;
                            default:
                                break;
                        }

                        boolean validDate = false;
                        scanner.nextLine();
                        while (!validDate) {
                            System.out.println("Enter release date of show (YYYY-MM-DD):");
                            String input = scanner.nextLine();

                            if (input.isEmpty()) {
                                System.out.println("Invalid input: input cannot be empty.");
                            } else if (!Pattern.matches("\\d{4}-\\d{2}-\\d{2}", input)) {
                                System.out.println("Invalid input: please enter a date in the format YYYY-MM-DD.");
                            } else {
                                try {
                                    LocalDate releaseDate = LocalDate.parse(input);
                                    currentShow.setReleaseDate(releaseDate);
                                    validDate = true;
                                } catch (Exception e) {
                                    System.out.println("Invalid input: the date you entered is not valid.");
                                }
                            }
                        }

                        System.out.println("Enter number of likes:");
                        currentShow.setLikes(scanner.nextInt());

                        platform.registerShow(currentShow);
                        break;

                    } else {
                        System.out.println("You found a backdoor but move on ! :)");
                    }
                    break;
                default:
                    break;
            }
            System.out.println("\nPress enter to continue...");
            scanner.nextLine();
            if (loopCase != 0) {
                try {
                    System.in.read();
                } catch (Exception e) {

                }
            }
        }

        scanner.close();
        System.out.println("\nGoodbye!");
    }

    public static void showLoginRegister() {
        System.out.println("1: Login");
        System.out.println("2: Register");
        System.out.println("0: Exit");
    }

    public static void showMenu() {
        System.out.println("1: View Shows");
        System.out.println("2: Like Show");
        System.out.println("3: Display personal feed. Sort by number of likes");
        System.out.println("4: Display personal feed. Sort by release date - newest");
    }

    public static void showUserMenu() {
        showMenu();
        System.out.println("0: Exit");
    }

    public static void showAdminMenu() {
        showMenu();
        System.out.println("5: Add show to database");
        System.out.println("0: Exit");
    }

    public static void showCategory() {
        System.out.println("1: Anime");
        System.out.println("2: Fantasy");
        System.out.println("3: Cartoon");
    }
}