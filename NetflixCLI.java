import java.util.*;

class Movie {
    String title;
    String genre;
    int duration;
    double rating;

    public Movie(String title, String genre, int duration, double rating) {
        this.title = title;
        this.genre = genre;
        this.duration = duration;
        this.rating = rating;
    }

    @Override
    public String toString() {
        return title + " (" + genre + "), " + duration + " min, Rating: " + rating;
    }
}

class User {
    String username;
    String password;
    ArrayList<Movie> viewingHistory;
    HashMap<String, Integer> genrePreferences;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.viewingHistory = new ArrayList<>();
        this.genrePreferences = new HashMap<>();
    }

    public void updateViewingHistory(Movie movie) {
        viewingHistory.add(movie);
        updateGenrePreferences(movie.genre);
    }

    private void updateGenrePreferences(String genre) {
        genrePreferences.put(genre, genrePreferences.getOrDefault(genre, 0) + 1);
    }

    public List<Movie> getRecommendations(List<Movie> movieList) {
        List<Movie> recommendations = new ArrayList<>();
        for (Movie movie : movieList) {
            if (!viewingHistory.contains(movie) && 
                genrePreferences.containsKey(movie.genre) &&
                genrePreferences.get(movie.genre) > 0) {
                recommendations.add(movie);
            }
        }
        return recommendations;
    }
}

public class NetflixCLI {
    private static List<Movie> movieList = new ArrayList<>();
    private static List<User> userList = new ArrayList<>();
    private static User currentUser = null;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        loadMovies();
        while (true) {
            showMainMenu();
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    login();
                    break;
                case 2:
                    register();
                    break;
                case 3:
                    exit();
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void loadMovies() {
        movieList.add(new Movie("The Shawshank Redemption", "Drama", 142, 9.3));
        movieList.add(new Movie("The Dark Knight", "Action", 152, 9.0));
        movieList.add(new Movie("Interstellar", "Sci-Fi", 169, 8.6));
        movieList.add(new Movie("The Matrix", "Sci-Fi", 136, 8.7));
        movieList.add(new Movie("Pulp Fiction", "Crime", 154, 8.9));
    
    }

    private static void showMainMenu() {
        System.out.println("\nWelcome to Netflix CHINGCHING!");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Exit");
        System.out.print("Enter # choice: ");
    }

    private static void login() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        for (User user : userList) {
            if (user.username.equals(username) && user.password.equals(password)) {
                currentUser = user;
                showUserMenu();
                return;
            }
        }
        System.out.println("Invalid username or password.");
    }

    private static void register() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        User newUser = new User(username, password);
        userList.add(newUser);
        System.out.println("Registration successful!");
        currentUser = newUser;
        showUserMenu();
    }

    private static void showUserMenu() {
        while (currentUser != null) {
            System.out.println("\nUser Menu - Welcome, " + currentUser.username + "!");
            System.out.println("1. View Movies");
            System.out.println("2. View Recommendations");
            System.out.println("3. View Viewing History");
            System.out.println("4. Logout");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    viewMovies();
                    break;
                case 2:
                    viewRecommendations();
                    break;
                case 3:
                    viewViewingHistory();
                    break;
                case 4:
                    currentUser = null;
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void viewMovies() {
        System.out.println("\nAvailable Movies:");
        for (int i = 0; i < movieList.size(); i++) {
            System.out.println((i + 1) + ". " + movieList.get(i));
        }

        System.out.print("Enter movie number to watch (or 0 to go back): ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice > 0 && choice <= movieList.size()) {
            Movie selectedMovie = movieList.get(choice - 1);
            System.out.println("You are now watching " + selectedMovie);
            currentUser.updateViewingHistory(selectedMovie);
        }
    }

    private static void viewRecommendations() {
        List<Movie> recommendations = currentUser.getRecommendations(movieList);
        if (recommendations.isEmpty()) {
            System.out.println("No recommendations available at this time.");
        } else {
            System.out.println("\nRecommended Movies:");
            for (Movie movie : recommendations) {
                System.out.println(movie);
            }
        }
    }

    private static void viewViewingHistory() {
        System.out.println("\nViewing History:");
        if (currentUser.viewingHistory.isEmpty()) {
            System.out.println("No movies watched yet.");
        } else {
            for (Movie movie : currentUser.viewingHistory) {
                System.out.println(movie);
            }
        }
    }

    private static void exit() {
        System.out.println("Thank you for using Netflix CHINGCHING!");
    }
}