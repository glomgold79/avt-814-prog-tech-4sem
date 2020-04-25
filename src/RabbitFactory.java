public class RabbitFactory implements AbstactFactory {

    @Override
    public Ordinary createOrdinary(int X, int Y, long BirthTime) { return new Ordinary(X, Y, BirthTime); }

    @Override
    public Albino createAlbino(int X, int Y, long BirthTime) { return new Albino(X, Y, BirthTime); }

}
