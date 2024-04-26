package project20280.stacksqueues;

class BracketChecker {
    private final String input;

    public BracketChecker(String in) {
        input = in;
    }

    public boolean check() {
        LinkedStack<Character> stack = new LinkedStack<>();

        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);
            if (ch == '[' || ch == '{' || ch == '(') { //pushing opening brackets into the stack
                stack.push(ch);
            } else if (ch == ']' || ch == '}' || ch == ')') {   //when its a closing bracket
                if (stack.isEmpty()) {                          //if the stack is empty closing brackets are not closing a bracket
                    return false;
                }
                char leftBracket = stack.pop();

                if(leftBracket == '[' && ch != ']'  ||              //checking if the brackets match
                        leftBracket == '{' && ch != '}'||
                        leftBracket == '(' && ch != ')'
                ){
                    return false;
                }
            }
        }

        if (!stack.isEmpty()) {                             //if the stack is not empty that means theres still unclosed brackets
            return false;
        } else {                                            //if it is empty its correct
            return true;
        }
    }

    public static void main(String[] args) {
        String[] inputs = {
                "[]]()()]", // not correct
                "c[d]", // correct\n" +
                "a{b[c]d}e", // correct\n" +
                "a{b(c]d}e", // not correct; ] doesn't match (\n" +
                "a[b{c}d]e}", // not correct; nothing matches final }\n" +
                "a{b(c) ", // // not correct; Nothing matches opening {
        };

        for (String input : inputs) {
            BracketChecker checker = new BracketChecker(input);
            System.out.println("Checking: " + input);
            System.out.println(checker.check());
        }
    }
}