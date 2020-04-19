class maleStudentsAI extends BaseAI {

    long movingTime = 0;

    @Override
    void move() {
        synchronized (linkedList) {

            for (int j = 0; j < linkedList.size(); j++) {
                student student = linkedList.get(j);

                if (student instanceof maleStudent) {
                    if (movingTime % 1000 == 0) { //смена направления раз в секунду
                        student.changeDirection();
                    }
                    student.move();

                }
            }
            movingTime += 50;
        }

    }
}