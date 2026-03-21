// ==========================================
// 1. CÁC INTERFACE ĐÍCH (TARGET INTERFACES)
// ==========================================
interface IJsonData {
    String getJsonData();
}

interface IXmlData {
    String getXmlData();
}

// ==========================================
// 2. CÁC LỚP CUNG CẤP DỮ LIỆU GỐC (ADAPTEES)
// ==========================================
class XmlAdaptee implements IXmlData {
    private String xmlContent;

    public XmlAdaptee(String xmlContent) {
        this.xmlContent = xmlContent;
    }

    @Override
    public String getXmlData() {
        return xmlContent;
    }
}

class JsonAdaptee implements IJsonData {
    private String jsonContent;

    public JsonAdaptee(String jsonContent) {
        this.jsonContent = jsonContent;
    }

    @Override
    public String getJsonData() {
        return jsonContent;
    }
}

// ==========================================
// 3. CÁC ADAPTER CHUYỂN ĐỔI (ADAPTERS)
// ==========================================

// Bộ chuyển đổi từ XML sang JSON
class XmlToJsonAdapter implements IJsonData {
    private IXmlData xmlData;

    public XmlToJsonAdapter(IXmlData xmlData) {
        this.xmlData = xmlData;
    }

    @Override
    public String getJsonData() {
        // Lấy dữ liệu XML gốc
        String xml = xmlData.getXmlData();
        System.out.println("[Adapter] Đang chuyển đổi XML sang JSON...");

        // Mô phỏng logic parse XML thành JSON
        String json = xml.replace("<user>", "{ \"user\": { ")
                .replace("</user>", " } }")
                .replace("<name>", "\"name\": \"")
                .replace("</name>", "\", ")
                .replace("<age>", "\"age\": ")
                .replace("</age>", " }");
        return json;
    }
}

// Bộ chuyển đổi từ JSON sang XML
class JsonToXmlAdapter implements IXmlData {
    private IJsonData jsonData;

    public JsonToXmlAdapter(IJsonData jsonData) {
        this.jsonData = jsonData;
    }

    @Override
    public String getXmlData() {
        // Lấy dữ liệu JSON gốc
        String json = jsonData.getJsonData();
        System.out.println("[Adapter] Đang chuyển đổi JSON sang XML...");

        // Mô phỏng logic parse JSON thành XML
        String xml = "<user><name>Nguyen Van A</name><age>22</age></user>"; // Giả lập kết quả
        return xml;
    }
}

// ==========================================
// 4. CLIENTS (CÁC HỆ THỐNG TIÊU THỤ DỮ LIỆU)
// ==========================================

// Web Service hiện đại, chỉ nhận JSON
class ModernWebService {
    public void processJsonRequest(IJsonData data) {
        System.out.println("[Web Service] Xử lý dữ liệu JSON: \n" + data.getJsonData());
    }
}

// Hệ thống cũ, chỉ nhận XML
class LegacySystem {
    public void processXmlRequest(IXmlData data) {
        System.out.println("[Legacy System] Xử lý dữ liệu XML: \n" + data.getXmlData());
    }
}

// ==========================================
// 5. HÀM CHẠY THỬ NGHIỆM (MAIN)
// ==========================================
public class AdapterFormatDemo {
    public static void main(String[] args) {
        ModernWebService webService = new ModernWebService();
        LegacySystem legacySystem = new LegacySystem();

        System.out.println("=== KỊCH BẢN 1: Web Service (cần JSON) đọc dữ liệu từ nguồn XML ===");
        IXmlData oldXmlData = new XmlAdaptee("<user><name>Nguyen Van A</name><age>22</age></user>");

        // Cắm Adapter vào: Truyền XML Data vào Adapter để nó biến thành JSON Data
        IJsonData adapterForWeb = new XmlToJsonAdapter(oldXmlData);
        webService.processJsonRequest(adapterForWeb);

        System.out.println("\n=== KỊCH BẢN 2: Legacy System (cần XML) đọc dữ liệu từ nguồn JSON ===");
        IJsonData newJsonData = new JsonAdaptee("{ \"user\": { \"name\": \"Tran Thi B\", \"age\": 25 } }");

        // Cắm Adapter vào: Truyền JSON Data vào Adapter để nó biến thành XML Data
        IXmlData adapterForLegacy = new JsonToXmlAdapter(newJsonData);
        legacySystem.processXmlRequest(adapterForLegacy);
    }
}