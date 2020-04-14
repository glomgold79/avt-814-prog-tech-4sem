public class RabbitFactory implements AbstactFactory {

    @Override
    public Ordinary creatOrdinary() { return new Ordinary(); }

    @Override
    public Ordinary creatOrdinary(double X, double Y, long BirthTime) { return new Ordinary(X, Y, BirthTime); }

    @Override
    public Albino creatAlbino() { return new Albino(); }

    @Override
    public Albino creatAlbino(double X, double Y, long BirthTime) { return new Albino(X, Y, BirthTime); }

}
