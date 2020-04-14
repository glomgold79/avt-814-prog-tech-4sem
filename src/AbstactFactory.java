public interface AbstactFactory {

    Ordinary creatOrdinary();
    Ordinary creatOrdinary(double X, double Y, long BirthTime);

    Albino creatAlbino();
    Albino creatAlbino(double X, double Y, long BirthTime);

}
