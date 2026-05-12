import { useEffect, useState } from "react";
import {
  ActivityIndicator,
  Alert,
  FlatList,
  Platform,
  Pressable,
  SafeAreaView,
  ScrollView,
  StyleSheet,
  StatusBar,
  Text,
  TextInput,
  View
} from "react-native";

const ORCHESTRATOR_URL = "http://192.168.1.10:8080";

type User = {
  id: string;
  name: string;
  email: string;
};

type Tour = {
  id: string;
  name: string;
  location: string;
  description: string;
  price: number;
  duration: string;
  availableSeats: number;
};

const fallbackTours: Tour[] = [
  {
    id: "t1",
    name: "Da Nang - Hoi An",
    location: "Da Nang",
    description: "Tour bien My Khe, Ba Na Hills va pho co Hoi An.",
    price: 3500000,
    duration: "3 ngay 2 dem",
    availableSeats: 20
  },
  {
    id: "t2",
    name: "Da Lat nghi duong",
    location: "Lam Dong",
    description: "Tham quan Lang Biang, thung lung tinh yeu va cho dem Da Lat.",
    price: 2900000,
    duration: "3 ngay 2 dem",
    availableSeats: 15
  },
  {
    id: "t3",
    name: "Phu Quoc bien dao",
    location: "Kien Giang",
    description: "Trai nghiem bien xanh, cap treo Hon Thom va am thuc hai san.",
    price: 5200000,
    duration: "4 ngay 3 dem",
    availableSeats: 12
  }
];

export default function App() {
  const [currentUser, setCurrentUser] = useState<User | null>(null);
  const [email, setEmail] = useState("an@example.com");
  const [password, setPassword] = useState("123456");
  const [selectedTour, setSelectedTour] = useState<Tour | null>(fallbackTours[0]);
  const [tours, setTours] = useState<Tour[]>(fallbackTours);
  const [quantity, setQuantity] = useState("1");
  const [loading, setLoading] = useState(false);
  const [loadingTours, setLoadingTours] = useState(false);
  const [result, setResult] = useState<string>("");

  useEffect(() => {
    async function loadTours() {
      setLoadingTours(true);
      try {
        const response = await fetch(`${ORCHESTRATOR_URL}/tours`);
        const data = await response.json();

        if (!response.ok) {
          throw new Error(data.message ?? "Khong the tai tour");
        }

        setTours(data);
        setSelectedTour(data[0] ?? null);
      } catch {
        setTours(fallbackTours);
        setSelectedTour(fallbackTours[0]);
      } finally {
        setLoadingTours(false);
      }
    }

    loadTours();
  }, []);

  async function login() {
    setLoading(true);

    try {
      const response = await fetch(`${ORCHESTRATOR_URL}/login`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, password })
      });
      const data = await response.json();

      if (!response.ok) {
        throw new Error(data.message ?? "Dang nhap that bai");
      }

      setCurrentUser(data.user);
      setResult("");
    } catch (error) {
      const message = error instanceof Error ? error.message : "Khong the ket noi Orchestrator";
      Alert.alert("Dang nhap that bai", message);
    } finally {
      setLoading(false);
    }
  }

  async function bookTour() {
    if (!currentUser || !selectedTour) {
      Alert.alert("Thieu thong tin", "Vui long dang nhap va chon tour");
      return;
    }

    setLoading(true);
    setResult("");

    try {
      const response = await fetch(`${ORCHESTRATOR_URL}/book-tour`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          userId: currentUser.id,
          tourId: selectedTour.id,
          quantity: Number(quantity)
        })
      });

      const data = await response.json();

      if (!response.ok) {
        throw new Error(data.detail ?? data.message ?? "Dat tour that bai");
      }

      setResult(`Xac nhan thanh cong: ${data.confirmation.booking.id}`);
    } catch (error) {
      const message = error instanceof Error ? error.message : "Khong the ket noi Orchestrator";
      setResult(`That bai: ${message}`);
    } finally {
      setLoading(false);
    }
  }

  const selectedTotal = selectedTour ? selectedTour.price * Math.max(Number(quantity) || 0, 0) : 0;

  return (
    <SafeAreaView style={styles.safeArea}>
      <StatusBar barStyle="dark-content" />
      <ScrollView contentContainerStyle={styles.page}>
        <View style={styles.shell}>
          <View style={styles.header}>
            <View>
              <Text style={styles.eyebrow}>Travel Booking System</Text>
              <Text style={styles.title}>Dat tour du lich</Text>
              <Text style={styles.subtitle}>Frontend web goi Orchestrator Service qua REST API</Text>
            </View>
            <View style={styles.statusPill}>
              <View style={styles.statusDot} />
              <Text style={styles.statusText}>Web ready</Text>
            </View>
          </View>

          {!currentUser ? (
            <View style={styles.loginGrid}>
              <View style={styles.introPanel}>
                <Text style={styles.panelKicker}>SOA orchestration</Text>
                <Text style={styles.introTitle}>Mot man hinh web de test flow dat tour end-to-end</Text>
                <Text style={styles.introCopy}>
                  Dang nhap bang tai khoan mau, chon tour, sau do frontend chi goi Orchestrator de thuc hien toan bo quy trinh.
                </Text>
                <View style={styles.metricRow}>
                  <View style={styles.metricItem}>
                    <Text style={styles.metricValue}>3</Text>
                    <Text style={styles.metricLabel}>Tours</Text>
                  </View>
                  <View style={styles.metricItem}>
                    <Text style={styles.metricValue}>4</Text>
                    <Text style={styles.metricLabel}>Services</Text>
                  </View>
                  <View style={styles.metricItem}>
                    <Text style={styles.metricValue}>REST</Text>
                    <Text style={styles.metricLabel}>API</Text>
                  </View>
                </View>
              </View>

              <View style={styles.panel}>
                <Text style={styles.sectionTitle}>Dang nhap</Text>
                <TextInput
                  autoCapitalize="none"
                  keyboardType="email-address"
                  onChangeText={setEmail}
                  placeholder="Email"
                  style={styles.input}
                  value={email}
                />
                <TextInput
                  onChangeText={setPassword}
                  placeholder="Password"
                  secureTextEntry
                  style={styles.input}
                  value={password}
                />
                <Pressable disabled={loading} onPress={login} style={({ pressed }) => [
                  styles.primaryButton,
                  pressed && styles.buttonPressed,
                  loading && styles.buttonDisabled
                ]}>
                  {loading ? (
                    <ActivityIndicator color="#fff" />
                  ) : (
                    <Text style={styles.primaryButtonText}>Dang nhap</Text>
                  )}
                </Pressable>
              </View>
            </View>
          ) : (
            <>
              <View style={styles.userBar}>
                <View>
                  <Text style={styles.userLabel}>Dang nhap voi</Text>
                  <Text style={styles.userName}>{currentUser.name}</Text>
                </View>
                <Pressable onPress={() => setCurrentUser(null)} style={styles.secondaryButton}>
                  <Text style={styles.secondaryButtonText}>Dang xuat</Text>
                </Pressable>
              </View>

              <View style={styles.contentGrid}>
                <View style={styles.catalogPanel}>
                  <View style={styles.sectionHeader}>
                    <View>
                      <Text style={styles.sectionTitle}>Danh sach tour</Text>
                      <Text style={styles.sectionHint}>Chon mot tour de tao booking</Text>
                    </View>
                    {loadingTours ? <ActivityIndicator color="#0f766e" /> : null}
                  </View>

                  <FlatList
                    data={tours}
                    keyExtractor={(item) => item.id}
                    numColumns={Platform.OS === "web" ? 2 : 1}
                    scrollEnabled={false}
                    columnWrapperStyle={Platform.OS === "web" ? styles.cardRow : undefined}
                    renderItem={({ item }) => {
                      const active = selectedTour?.id === item.id;
                      return (
                        <Pressable
                          onPress={() => setSelectedTour(item)}
                          style={({ pressed }) => [
                            styles.tourCard,
                            active && styles.tourCardActive,
                            pressed && styles.cardPressed
                          ]}
                        >
                          <View style={styles.tourCardTop}>
                            <Text style={styles.tourName}>{item.name}</Text>
                            <Text style={styles.locationBadge}>{item.location}</Text>
                          </View>
                          <Text style={styles.tourDescription}>{item.description}</Text>
                          <View style={styles.tourFooter}>
                            <View>
                              <Text style={styles.price}>{item.price.toLocaleString("vi-VN")} VND</Text>
                              <Text style={styles.tourMeta}>{item.duration}</Text>
                            </View>
                            <Text style={styles.seatText}>{item.availableSeats} cho</Text>
                          </View>
                        </Pressable>
                      );
                    }}
                  />
                </View>

                <View style={styles.bookingPanel}>
                  <Text style={styles.sectionTitle}>Dat tour</Text>
                  <View style={styles.summaryBox}>
                    <Text style={styles.summaryLabel}>Tour dang chon</Text>
                    <Text style={styles.selectedText}>{selectedTour?.name}</Text>
                    <Text style={styles.summaryMeta}>{selectedTour?.duration} tai {selectedTour?.location}</Text>
                  </View>
                  <TextInput
                    keyboardType="number-pad"
                    onChangeText={setQuantity}
                    placeholder="So luong"
                    style={styles.input}
                    value={quantity}
                  />
                  <View style={styles.totalRow}>
                    <Text style={styles.totalLabel}>Tam tinh</Text>
                    <Text style={styles.totalValue}>{selectedTotal.toLocaleString("vi-VN")} VND</Text>
                  </View>
                  <Pressable disabled={loading} onPress={bookTour} style={({ pressed }) => [
                    styles.primaryButton,
                    pressed && styles.buttonPressed,
                    loading && styles.buttonDisabled
                  ]}>
                    {loading ? (
                      <ActivityIndicator color="#fff" />
                    ) : (
                      <Text style={styles.primaryButtonText}>Dat va thanh toan</Text>
                    )}
                  </Pressable>
                  {result ? (
                    <Text style={[styles.resultText, result.startsWith("That bai") && styles.errorText]}>
                      {result}
                    </Text>
                  ) : null}

                  <View style={styles.endpointBox}>
                    <Text style={styles.summaryLabel}>Orchestrator</Text>
                    <Text style={styles.endpointText}>{ORCHESTRATOR_URL}</Text>
                  </View>
                </View>
              </View>
            </>
          )}
        </View>
      </ScrollView>
    </SafeAreaView>
  );
}

const baseShadow = Platform.select({
  web: {
    boxShadow: "0 16px 40px rgba(15, 23, 42, 0.08)"
  },
  default: {
    elevation: 3,
    shadowColor: "#172033",
    shadowOpacity: 0.08,
    shadowRadius: 12
  }
});

const styles = StyleSheet.create({
  safeArea: {
    flex: 1,
    backgroundColor: "#eef3f8"
  },
  page: {
    flexGrow: 1,
    paddingHorizontal: 24,
    paddingVertical: 28
  },
  shell: {
    alignSelf: "center",
    maxWidth: 1180,
    width: "100%"
  },
  header: {
    alignItems: "flex-start",
    backgroundColor: "#ffffff",
    borderColor: "#dce4ee",
    borderRadius: 8,
    borderWidth: 1,
    flexDirection: Platform.OS === "web" ? "row" : "column",
    gap: 18,
    justifyContent: "space-between",
    marginBottom: 20,
    padding: 24,
    ...baseShadow
  },
  eyebrow: {
    color: "#0f766e",
    fontSize: 13,
    fontWeight: "800",
    marginBottom: 8,
    textTransform: "uppercase"
  },
  title: {
    color: "#102033",
    fontSize: 34,
    fontWeight: "800"
  },
  subtitle: {
    color: "#5d6b7d",
    fontSize: 15,
    marginTop: 8
  },
  statusPill: {
    alignItems: "center",
    backgroundColor: "#ecfdf5",
    borderColor: "#b7e4d6",
    borderRadius: 999,
    borderWidth: 1,
    flexDirection: "row",
    gap: 8,
    paddingHorizontal: 12,
    paddingVertical: 8
  },
  statusDot: {
    backgroundColor: "#0f766e",
    borderRadius: 5,
    height: 10,
    width: 10
  },
  statusText: {
    color: "#0f513d",
    fontSize: 13,
    fontWeight: "700"
  },
  loginGrid: {
    alignItems: "stretch",
    flexDirection: Platform.OS === "web" ? "row" : "column",
    gap: 20
  },
  introPanel: {
    backgroundColor: "#102033",
    borderRadius: 8,
    flex: 1.3,
    justifyContent: "space-between",
    minHeight: 330,
    padding: 28
  },
  panelKicker: {
    color: "#7dd3c7",
    fontSize: 13,
    fontWeight: "800",
    textTransform: "uppercase"
  },
  introTitle: {
    color: "#ffffff",
    fontSize: 30,
    fontWeight: "800",
    lineHeight: 38,
    marginTop: 18,
    maxWidth: 620
  },
  introCopy: {
    color: "#c9d4e2",
    fontSize: 16,
    lineHeight: 24,
    marginTop: 14,
    maxWidth: 640
  },
  metricRow: {
    flexDirection: "row",
    flexWrap: "wrap",
    gap: 12,
    marginTop: 28
  },
  metricItem: {
    backgroundColor: "#182b42",
    borderColor: "#2c415b",
    borderRadius: 8,
    borderWidth: 1,
    minWidth: 112,
    padding: 14
  },
  metricValue: {
    color: "#ffffff",
    fontSize: 22,
    fontWeight: "800"
  },
  metricLabel: {
    color: "#9fb0c4",
    fontSize: 13,
    marginTop: 4
  },
  panel: {
    backgroundColor: "#ffffff",
    borderColor: "#dce4ee",
    borderRadius: 8,
    borderWidth: 1,
    flex: 0.7,
    gap: 14,
    minWidth: Platform.OS === "web" ? 360 : undefined,
    padding: 22,
    ...baseShadow
  },
  userBar: {
    alignItems: "center",
    backgroundColor: "#ffffff",
    borderColor: "#dce4ee",
    borderRadius: 8,
    borderWidth: 1,
    flexDirection: "row",
    justifyContent: "space-between",
    marginBottom: 18,
    padding: 18
  },
  userLabel: {
    color: "#6b788a",
    fontSize: 13
  },
  userName: {
    color: "#102033",
    fontSize: 18,
    fontWeight: "800",
    marginTop: 3
  },
  contentGrid: {
    alignItems: "flex-start",
    flexDirection: Platform.OS === "web" ? "row" : "column",
    gap: 20
  },
  catalogPanel: {
    backgroundColor: "#ffffff",
    borderColor: "#dce4ee",
    borderRadius: 8,
    borderWidth: 1,
    flex: 1,
    padding: 20,
    width: "100%",
    ...baseShadow
  },
  bookingPanel: {
    backgroundColor: "#ffffff",
    borderColor: "#dce4ee",
    borderRadius: 8,
    borderWidth: 1,
    gap: 14,
    padding: 20,
    width: Platform.OS === "web" ? 360 : "100%",
    ...baseShadow
  },
  sectionHeader: {
    alignItems: "center",
    flexDirection: "row",
    justifyContent: "space-between",
    marginBottom: 16
  },
  sectionTitle: {
    color: "#102033",
    fontSize: 20,
    fontWeight: "800"
  },
  sectionHint: {
    color: "#6b788a",
    fontSize: 14,
    marginTop: 4
  },
  cardRow: {
    gap: 14
  },
  tourCard: {
    backgroundColor: "#fbfcfe",
    borderColor: "#dce4ee",
    borderRadius: 8,
    borderWidth: 1,
    flex: 1,
    marginBottom: 14,
    minHeight: 190,
    padding: 16
  },
  tourCardActive: {
    backgroundColor: "#f0fdfa",
    borderColor: "#0f766e",
    borderWidth: 2
  },
  cardPressed: {
    opacity: 0.88
  },
  tourCardTop: {
    alignItems: "flex-start",
    flexDirection: "row",
    gap: 10,
    justifyContent: "space-between"
  },
  tourName: {
    color: "#102033",
    flex: 1,
    fontSize: 18,
    fontWeight: "800"
  },
  locationBadge: {
    backgroundColor: "#e8eef6",
    borderRadius: 999,
    color: "#41516a",
    fontSize: 12,
    fontWeight: "700",
    overflow: "hidden",
    paddingHorizontal: 10,
    paddingVertical: 5
  },
  tourDescription: {
    color: "#435167",
    fontSize: 14,
    lineHeight: 21,
    marginTop: 12
  },
  tourFooter: {
    alignItems: "flex-end",
    flexDirection: "row",
    justifyContent: "space-between",
    marginTop: "auto",
    paddingTop: 18
  },
  price: {
    color: "#a64224",
    fontSize: 17,
    fontWeight: "800"
  },
  tourMeta: {
    color: "#6b788a",
    fontSize: 13,
    marginTop: 4
  },
  seatText: {
    color: "#0f766e",
    fontSize: 13,
    fontWeight: "800"
  },
  input: {
    backgroundColor: "#f6f8fb",
    borderColor: "#cfd8e5",
    borderRadius: 8,
    borderWidth: 1,
    color: "#102033",
    fontSize: 16,
    minHeight: 46,
    paddingHorizontal: 12,
    paddingVertical: 11
  },
  primaryButton: {
    alignItems: "center",
    backgroundColor: "#0f766e",
    borderRadius: 8,
    minHeight: 48,
    justifyContent: "center",
    paddingHorizontal: 16
  },
  buttonPressed: {
    opacity: 0.9
  },
  buttonDisabled: {
    opacity: 0.65
  },
  primaryButtonText: {
    color: "#ffffff",
    fontSize: 16,
    fontWeight: "800"
  },
  secondaryButton: {
    backgroundColor: "#edf4f8",
    borderColor: "#d4e0ea",
    borderRadius: 8,
    borderWidth: 1,
    paddingHorizontal: 14,
    paddingVertical: 10
  },
  secondaryButtonText: {
    color: "#29435b",
    fontSize: 14,
    fontWeight: "800"
  },
  summaryBox: {
    backgroundColor: "#f6f8fb",
    borderColor: "#dce4ee",
    borderRadius: 8,
    borderWidth: 1,
    gap: 4,
    padding: 14
  },
  summaryLabel: {
    color: "#6b788a",
    fontSize: 12,
    fontWeight: "800",
    textTransform: "uppercase"
  },
  selectedText: {
    color: "#102033",
    fontSize: 17,
    fontWeight: "800"
  },
  summaryMeta: {
    color: "#5d6b7d",
    fontSize: 13
  },
  totalRow: {
    alignItems: "center",
    flexDirection: "row",
    justifyContent: "space-between"
  },
  totalLabel: {
    color: "#6b788a",
    fontSize: 14,
    fontWeight: "700"
  },
  totalValue: {
    color: "#102033",
    fontSize: 16,
    fontWeight: "800"
  },
  resultText: {
    backgroundColor: "#eef9f5",
    borderColor: "#b7e4d6",
    borderRadius: 8,
    borderWidth: 1,
    color: "#0f513d",
    lineHeight: 20,
    padding: 12
  },
  errorText: {
    backgroundColor: "#fff1f0",
    borderColor: "#ffc8c2",
    color: "#9b2f21"
  },
  endpointBox: {
    borderTopColor: "#e2e8f0",
    borderTopWidth: 1,
    gap: 4,
    marginTop: 4,
    paddingTop: 14
  },
  endpointText: {
    color: "#435167",
    fontSize: 13
  }
});
