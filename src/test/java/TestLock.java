import java.util.List;

public class TestLock {
    public static void main(String[] args) {
//        List<Integer> result = Arrays.asList(2, 2, 2, 3);
////        result.stream().takeWhile(i -> i<=2).forEach(System.out::println);
////        result.stream().dropWhile(i -> i <=2).forEach(System.out::println);
//
//        A a1 = new A("a1", Arrays.asList(new B("b1"), new B("b2")));
//        A a2 = new A("a2", Arrays.asList(new B("b1"), new B("b4")));
//        A a3 = new A("a3", Arrays.asList(new B("b1"), new B("b6")));
//        List<A> list = Arrays.asList(a1, a2, a3);
//
//        list.stream().collect(groupingBy(A::getString, flatMapping(a -> a.getbList().stream(), filtering(b->b.getAddress().equals("b1"),toSet()))))
//                .forEach((k, v) -> System.out.println(k + " -> " + v));
    }

    static class A {
        String string;
        List<B> bList;

        public A(String string, List<B> bList) {
            this.string = string;
            this.bList = bList;
        }

        public String getString() {
            return string;
        }

        public void setString(String string) {
            this.string = string;
        }

        public List<B> getbList() {
            return bList;
        }

        public void setbList(List<B> bList) {
            this.bList = bList;
        }
    }

    static class B {
        String address;

        public B(String address) {
            this.address = address;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        @Override
        public String toString() {
            return "B{" +
                    "address='" + address + '\'' +
                    '}';
        }
    }
}
