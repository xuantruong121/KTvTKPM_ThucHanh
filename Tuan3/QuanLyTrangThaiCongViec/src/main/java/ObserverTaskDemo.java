import java.util.ArrayList;
import java.util.List;

// 1. Observer Interface
interface TaskObserver {
    void update(String taskName, String status);
}

// 2. Subject Interface
interface TaskSubject {
    void addSubscriber(TaskObserver observer);
    void removeSubscriber(TaskObserver observer);
    void notifySubscribers();
}

// 3. Concrete Subject (Công việc cụ thể)
class Task implements TaskSubject {
    private String name;
    private String status;
    private List<TaskObserver> teamMembers;

    public Task(String name) {
        this.name = name;
        this.status = "To Do"; // Trạng thái mặc định
        this.teamMembers = new ArrayList<>();
    }

    @Override
    public void addSubscriber(TaskObserver observer) {
        teamMembers.add(observer);
    }

    @Override
    public void removeSubscriber(TaskObserver observer) {
        teamMembers.remove(observer);
    }

    @Override
    public void notifySubscribers() {
        for (TaskObserver member : teamMembers) {
            member.update(name, status);
        }
    }

    // Hành động thay đổi trạng thái công việc
    public void setStatus(String status) {
        System.out.println("\n[TRELLO/JIRA BOT] Task '" + name + "' đã được chuyển sang trạng thái: " + status);
        this.status = status;
        notifySubscribers();
    }
}

// 4. Concrete Observer (Thành viên dự án)
class TeamMember implements TaskObserver {
    private String name;
    private String role;

    public TeamMember(String name, String role) {
        this.name = name;
        this.role = role;
    }

    @Override
    public void update(String taskName, String status) {
        System.out.println(" -> Email gửi tới [" + role + " - " + name + "]: Bạn có 1 cập nhật mới cho Task '" + taskName + "' (Status: " + status + ")");
    }
}

// 5. Client
public class ObserverTaskDemo {
    public static void main(String[] args) {
        System.out.println("=== HỆ THỐNG THÔNG BÁO DỰ ÁN ===");

        // Tạo một Task mới
        Task dbTask = new Task("Thiết kế Database Schema");

        // Thêm các thành viên vào dự án
        TaskObserver dev = new TeamMember("Nguyễn Văn A", "Backend Dev");
        TaskObserver pm = new TeamMember("Trần Thị B", "Project Manager");
        TaskObserver tester = new TeamMember("Lê Văn C", "QA/Tester");

        // Các thành viên Subscribe (Theo dõi) task này
        dbTask.addSubscriber(dev);
        dbTask.addSubscriber(pm);
        dbTask.addSubscriber(tester);

        // Dev bắt đầu làm việc
        dbTask.setStatus("In Progress");

        // Dev làm xong, chuyển cho Tester kiểm tra. PM không cần theo dõi chi tiết bước này nữa nên Unsubscribe.
        System.out.println("\n[HỆ THỐNG] Project Manager ngừng theo dõi Task này.");
        dbTask.removeSubscriber(pm);

        dbTask.setStatus("In Testing");
    }
}