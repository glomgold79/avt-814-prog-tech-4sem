public interface AbstactFactory {

    Ordinary createOrdinary(int X, int Y, long BirthTime);

    Albino createAlbino(int X, int Y, long BirthTime);

}
