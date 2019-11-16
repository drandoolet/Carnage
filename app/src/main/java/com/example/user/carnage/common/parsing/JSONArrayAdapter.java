package com.example.user.carnage.common.parsing;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.EmptyStackException;

public class JSONArrayAdapter {
    private final JSONArray array;

    public JSONArrayAdapter(JSONArray array) {
        this.array = array;
    }

    public <T> Iterator iterator() {
        return new Iterator<T>(array);
    }

    public static class Iterator<T> {
        private final JSONArray array;
        private final int length;
        private int pos = 0;

        private <T> Iterator(JSONArray array) {
            this.array = array;
            length = array.length();
        }

        @SuppressWarnings("unchecked")
        public T next() throws JSONException, EmptyStackException {
            if (hasNext()) return (T) array.get(++pos);
            else throw new EmptyStackException();
        }

        public boolean hasNext() {
            return pos < length -1;
        }
    }
}
