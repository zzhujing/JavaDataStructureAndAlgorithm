package com.concurrent.design.activeobject;

public class DisplayStringRequest extends MethodRequest {

    private final String text;

    protected DisplayStringRequest(Servant servant, final String text) {
        super(servant, null);
        this.text = text;
    }

    @Override
    public void execute() {
        servant.displayString(text);
    }
}
