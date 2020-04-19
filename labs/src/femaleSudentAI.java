class femaleStudentsAI extends BaseAI {
    @Override
    void move() {
        synchronized (linkedList) {
            for (int j = 0; j < linkedList.size(); j++) {
                student student = linkedList.get(j);
                if (student instanceof femaleStudent) {
                    student.move();
                }
            }
        }

    }
}