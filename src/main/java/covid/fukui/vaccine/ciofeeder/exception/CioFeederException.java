package covid.fukui.vaccine.ciofeeder.exception;

/**
 * CioFeederの独自例外
 */
public class CioFeederException extends RuntimeException {

    private static final long serialVersionUID = 8714300265825876835L;

    /**
     * メッセージ付きコンストラクタ
     *
     * @param message メッセージ
     */
    public CioFeederException(final String message) {
        super(message);
    }

    /**
     * メッセージと例外付きコンストラクタ
     *
     * @param message   メッセージ
     * @param throwable 例外
     */
    public CioFeederException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
