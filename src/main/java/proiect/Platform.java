package proiect;

import enums.CategoryEnum;
import enums.RoleEnum;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

public class Platform {
    ArrayList<User> users = new ArrayList<User>();
    ArrayList<Show> shows = new ArrayList<Show>();

    public Platform() {
        //Getting all users from the file
        readUsersFile();
        //Getting all shows from the file
        readShowsFile();
        //shuffle for random
    }

    private void readUsersFile() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src\\main\\java\\files\\users.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String username = parts[0];
                String password = parts[1];
                RoleEnum role = RoleEnum.valueOf(parts[2]);
                User user = new User(username, password, role);
                this.users.add(user);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        } catch (IOException e) {
            System.out.println("Error reading file.");
            e.printStackTrace();
        }
    }

    private void readShowsFile() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("src\\main\\java\\files\\shows.txt"));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split(",");
                String title = parts[0];
                CategoryEnum category = CategoryEnum.valueOf(parts[1]);

                LocalDate releaseDate = null;
                try {
                    releaseDate = LocalDate.parse(parts[2], formatter);
                } catch (DateTimeParseException e) {
                    System.out.println("Invalid date format in line: " + line);
                    continue;
                }

                int likes = Integer.parseInt(parts[3]);
                Show show = new Show(title, category, releaseDate, likes);
                this.shows.add(show);
            }
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        } catch (IOException e) {
            System.out.println("Error reading file.");
            e.printStackTrace();
        }
    }


    public int loginUser(String username, String password) {
        boolean foundUser = false;
        boolean foundPassword = false;
        RoleEnum role = RoleEnum.USER;
        for (User user : this.users) {
            if (username.equals(user.getUsername())) {
                foundUser = true;
                if (password.equals(user.getPassword())) {
                    foundPassword = true;
                    if (user.getRole().equals(RoleEnum.ADMIN)) {
                        role = user.getRole();
                    }
                }
                break;
            }
        }
        if (foundUser && foundPassword) {
            System.out.println("Logging in...");
            if (role.equals(RoleEnum.ADMIN)) {
                return 1;
            }
            return 2;
        }
        if (foundUser) {
            System.out.println("Password not found");
            return 0;
        } else {
            System.out.println("Username not found");
        }
        return 0;
    }

    public void registerUser(@NotNull User currentUser) {
        File file = new File("src\\main\\java\\files\\users.txt");
        try {
            FileWriter fileWriter = new FileWriter(file, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write
                    ("\n" + currentUser.getUsername() + ","
                            + currentUser.getPassword() + ","
                            + currentUser.getRole());
            bufferedWriter.close();
            System.out.println("Text appended to file successfully");
        } catch (IOException e) {
            System.out.println("Error appending text to file");
            e.printStackTrace();
        }
        readUsersFile();
    }

    public void registerShow(@NotNull Show currentShow) {
        File file = new File("src\\main\\java\\files\\shows.txt");
        try {
            FileWriter fileWriter = new FileWriter(file, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write
                    ("\n" + currentShow.getTitle() + ","
                            + currentShow.getCategory() + ","
                            + currentShow.getReleaseDate() + ","
                            + currentShow.getLikes());
            bufferedWriter.close();
            System.out.println("Text appended to file successfully");
        } catch (IOException e) {
            System.out.println("Error appending text to file");
            e.printStackTrace();
        }
        readShowsFile();
    }

    public void displayShows() {
        String leftAlignFormat = "| %-35s | %-10s | %-10s | %-6d |%n";
        System.out.format("+-------------------------------------+------------+------------+--------+%n");
        System.out.format("| Title                               | Category   | Release    | Likes  |%n");
        System.out.format("+-------------------------------------+------------+------------+--------+%n");
        for (Show show : this.shows) {
            System.out.format(leftAlignFormat,
                    show.getTitle(),
                    show.getCategory(),
                    show.getReleaseDate(),
                    show.getLikes());
        }
        System.out.format("+-------------------------------------+------------+------------+--------+%n");

    }

    public void feedBy(@NotNull String showTitle, Comparator<Show> showComparator) {
        if (showTitle.isEmpty()) {
            System.out.println("You dont have preferences.");
            return;
        }
        Optional<CategoryEnum> optionalCategory = shows.stream()
                .filter(show -> show.getTitle().equals(showTitle))
                .map(Show::getCategory)
                .findFirst();

        if (optionalCategory.isPresent()) {
            CategoryEnum category = optionalCategory.get();
            List<Show> sortedShows = shows.stream()
                    .sorted(Comparator.comparingInt((Show show) -> show.getCategory().equals(category) ? 0 : 1)
                            .thenComparing(showComparator))
                    .collect(Collectors.toList());

            shows.clear();
            shows.addAll(sortedShows);

            displayShows();
        } else {
            System.out.println("The show '" + showTitle + "' was not found.");
        }
    }

    public void feedByShow(@NotNull String showTitle) {
        feedBy(showTitle, (show1, show2) -> 0);
    }

    public void feedByLikes(@NotNull String showTitle) {
        feedBy(showTitle, Comparator.comparingInt(Show::getLikes).reversed());
    }

    public void feedByRelease(@NotNull String showTitle) {
        feedBy(showTitle, Comparator.comparing(Show::getReleaseDate).reversed());
    }

    public void displayShuffledShows() {
        Collections.shuffle(this.shows);
        displayShows();
    }

    public ArrayList<User> getUsers() {
        return users;
    }
}
