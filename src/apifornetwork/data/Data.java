package apifornetwork.data;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class Data {

    private ByteArrayInputStream inByte;

    public Data(final byte[] dataBytes) {
        this.inByte = new ByteArrayInputStream(dataBytes);
    }

    public Object getObject(Object typeAtRend) throws IOException, ClassNotFoundException {
        inByte.reset();
        Object obj = new ObjectInputStream(inByte).readUnshared();
        if (obj.getClass().getName().equals(typeAtRend.getClass().getName()))
            return obj;
        throw new ClassNotFoundException("The class asked isn't found !");
    }

}
