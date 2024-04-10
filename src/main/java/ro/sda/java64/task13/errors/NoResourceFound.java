package ro.sda.java64.task13.errors;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NoResourceFound extends RuntimeException{
    public NoResourceFound(String s) {
        super(s);
    }
}
