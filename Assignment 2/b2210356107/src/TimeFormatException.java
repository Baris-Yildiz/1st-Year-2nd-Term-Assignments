/**
 * TimeFormatException gets thrown in time format related inconveniences.
 */
class TimeFormatException extends RuntimeException {
    public TimeFormatException(String message) {
        super(message);
    }
}