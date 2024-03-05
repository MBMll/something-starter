#doc# syntax=docker/dockerfile:1

# build
FROM rust:latest as builder
WORKDIR /app

COPY ./rust-app ./

RUN cargo build --release


# deploy
FROM alpine:latest

RUN adduser \
    --disabled-password \
    --gecos "" \
    --home "/nonexistent" \
    --shell "/sbin/nologin" \
    --no-create-home \
    --uid "${UID}" \
    appuser
USER appuser

COPY --from=builder /app/target/rust-app /app


CMD /app
