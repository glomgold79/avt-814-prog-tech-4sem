public class RabbitFactory implements AbstactFactory {

    @Override
    public Ordinary creatOrdinary() { return new Ordinary(); }

    @Override
    public Ordinary creatOrdinary(double X, double Y) { return new Ordinary(X, Y); }

    @Override
    public Albino creatAlbino() { return new Albino(); }

    @Override
    public Albino creatAlbino(double X, double Y) { return new Albino(X, Y); }

}
