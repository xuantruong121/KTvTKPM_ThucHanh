FROM golang:1.22-alpine AS builder

WORKDIR /app

COPY go.mod main.go ./
ENV CGO_ENABLED=0
RUN go build -ldflags="-s -w" -o /app/server .

FROM alpine:3.20

RUN apk add --no-cache ca-certificates

WORKDIR /app
COPY --from=builder /app/server ./server

EXPOSE 8080

USER nobody

CMD ["./server"]
