import * as http from "http";

const port = process.env.PORT ? parseInt(process.env.PORT, 10) : 3000;

const server = http.createServer((req, res) => {
  res.writeHead(200, { "Content-Type": "text/plain; charset=utf-8" });
  res.end("Hello from Bai6 (multi-stage)!");
});

server.listen(port, "0.0.0.0", () => {
  console.log(`Server listening on port ${port}`);
});

