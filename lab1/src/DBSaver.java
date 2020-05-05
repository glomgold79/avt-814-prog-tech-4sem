import java.sql.*;

public class DBSaver {

    private static final String URL = "jdbc:mysql://localhost:3306/mydbrabbits";
    private static final String USER = "root";
    private static final String PASSWORD = "2046";

    public DBSaver() { }

    public void SaveAll() {
        DeleteAll();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement("insert into rabbit values(?,?,?,?)")) {
            for (int i = 0; i < Singleton.getVector().size(); i++) {
                preparedStatement.setInt(1,0);
                if (Singleton.getVector().elementAt(i) instanceof Albino)
                    preparedStatement.setString(2,"Albino");
                else
                    preparedStatement.setString(2,"Ordinary");
                preparedStatement.setInt(3,Singleton.getVector().elementAt(i).getX());
                preparedStatement.setInt(4,Singleton.getVector().elementAt(i).getY());

                preparedStatement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void SaveAlbino() {
        DeleteAll();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement("insert into rabbit values(?,?,?,?)")) {
            for (int i = 0; i < Singleton.getVector().size(); i++) {
                if (Singleton.getVector().elementAt(i) instanceof Albino) {
                    preparedStatement.setInt(1, 0);
                    preparedStatement.setString(2, "Albino");
                    preparedStatement.setInt(3, Singleton.getVector().elementAt(i).getX());
                    preparedStatement.setInt(4, Singleton.getVector().elementAt(i).getY());

                    preparedStatement.execute();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void SaveOrdinary() {
        DeleteAll();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement("insert into rabbit values(?,?,?,?)")) {
            for (int i = 0; i < Singleton.getVector().size(); i++) {
                if (Singleton.getVector().elementAt(i) instanceof Ordinary) {
                    preparedStatement.setInt(1, 0);
                    preparedStatement.setString(2, "Ordinary");
                    preparedStatement.setInt(3, Singleton.getVector().elementAt(i).getX());
                    preparedStatement.setInt(4, Singleton.getVector().elementAt(i).getY());

                    preparedStatement.execute();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void LoadAll() {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("select * from rabbit");
            while (resultSet.next()) {
                if (resultSet.getString(2).equals("Albino"))
                    Singleton.getVector().add(new Albino(resultSet.getInt(3),resultSet.getInt(4),Habitat.gui.cumulativePeriod));
                else
                    Singleton.getVector().add(new Ordinary(resultSet.getInt(3),resultSet.getInt(4),Habitat.gui.cumulativePeriod));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void LoadAlbino() {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("select * from rabbit where type = 'Albino'");
            while (resultSet.next()) {
                Singleton.getVector().add(new Albino(resultSet.getInt(3),resultSet.getInt(4),Habitat.gui.cumulativePeriod));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void LoadOrdinary() {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("select * from rabbit where type = 'Ordinary'");
            while (resultSet.next()) {
                Singleton.getVector().add(new Ordinary(resultSet.getInt(3),resultSet.getInt(4),Habitat.gui.cumulativePeriod));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void DeleteAll() {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement()) {
            statement.execute("delete from rabbit");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
