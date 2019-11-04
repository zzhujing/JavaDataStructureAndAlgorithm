package com.concurrent.design.activeobject;

public class MakeStringRequest extends MethodRequest {

    private final int count;
    private final char filler;

    protected MakeStringRequest(Servant servant, FutureResult futureResult, int count, char filler) {
        super(servant, futureResult);
        this.count = count;
        this.filler = filler;
    }

    @Override
    public void execute() {
        Result result = servant.makeString(count, filler);
        futureResult.setResult(result);
    }
}
