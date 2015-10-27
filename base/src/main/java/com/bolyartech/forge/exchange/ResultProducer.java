package com.bolyartech.forge.exchange;

/**
 * Created by ogre on 27.10.15.
 */
public interface ResultProducer {
    /**
     * Creates object <code>T</code> from JSON string
     *
     * @param str   The string that contains json
     * @param clazz Class of the object to create
     * @return The newly created object of type clazz
     * @throws ResultProducerException If the string cannot be processed to produce result of type T
     */
    <T> T produce(String str, Class<T> clazz) throws ResultProducerException;


    @SuppressWarnings("serial")
    class ResultProducerException extends RuntimeException {

        public ResultProducerException() {
            super();
        }


        public ResultProducerException(String message, Throwable cause) {
            super(message, cause);
        }


        public ResultProducerException(String message) {
            super(message);
        }


        public ResultProducerException(Throwable cause) {
            super(cause);
        }
    }
}
