/**
 *
 */
package guacaloco.core;

public class VsphereToolkitException extends Exception {

    private static final long serialVersionUID = -7431463701213992549L;

    /**
     * @param message
     */
    public VsphereToolkitException(String message) {
        super(message);
    }

    /**
     * @param message
     * @param cause
     */
    public VsphereToolkitException(String message, Throwable cause) {
        super(message, cause);
    }

}
