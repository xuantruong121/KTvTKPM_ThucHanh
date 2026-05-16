package iuh.fit.orderservice.resilience;

/**
 * Business exception - không trigger Retry (được khai báo trong ignoreExceptions).
 * Ném exception này khi lỗi do nghiệp vụ (không cần retry).
 */
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
