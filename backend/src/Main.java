//import com.sun.net.httpserver.HttpServer;
//import com.sun.net.httpserver.HttpExchange;
//import com.sun.net.httpserver.HttpHandler;
//import com.google.gson.Gson;
//
//import java.io.IOException;
//import java.io.OutputStream;
//import java.net.InetSocketAddress;
//import java.nio.charset.StandardCharsets;
//import java.util.HashMap;
//import java.util.Map;
//
//// Account model
//
//
//// Fake in-memory database
//
//
//public class Main {
//    private static final Gson gson = new Gson();
//
//    public static void main(String[] args) throws IOException {
//        HttpServer server = HttpServer.create(new InetSocketAddress(8880), 0);
//
//        server.createContext("/create", new CreateHandler());
//        server.createContext("/view", new ViewHandler());
//        server.createContext("/deposit", new DepositHandler());
//        server.createContext("/withdraw", new WithdrawHandler());
//        server.createContext("/balance", new BalanceHandler());
//        server.createContext("/register", new RegisterHandler());
//        server.createContext("/login", new LoginHandler());
//
//        server.setExecutor(null);
//        System.out.println("Server running on http://localhost:8880/");
//        server.start();
//    }
//
//    // ---------- Handlers ----------
//
//    static class CreateHandler implements HttpHandler {
//        public void handle(HttpExchange exchange) throws IOException {
//            if ("OPTIONS".equals(exchange.getRequestMethod())) {
//                sendCorsHeaders(exchange);
//                exchange.sendResponseHeaders(204, -1); // No content
//                return;
//            }
//
//            if ("POST".equals(exchange.getRequestMethod())) {
//                String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
//
//                Account acc = gson.fromJson(body, Account.class);
//                AccountDatabase.createAccount(acc.getId(), acc.getName(), acc.getBalance());
//
//                String response = gson.toJson(Map.of("status", "Account created", "id", acc.getId()));
//                sendResponse(exchange, response);
//            }
//        }
//    }
//
//    static class ViewHandler implements HttpHandler {
//        public void handle(HttpExchange exchange) throws IOException {
//            if ("OPTIONS".equals(exchange.getRequestMethod())) {
//                sendCorsHeaders(exchange);
//                exchange.sendResponseHeaders(204, -1);
//                return;
//            }
//
//            if ("GET".equals(exchange.getRequestMethod())) {
//                String query = exchange.getRequestURI().getQuery();
//                String id = query.split("=")[1];
//
//                Account acc = AccountDatabase.getAccount(id);
//                String response = (acc != null) ? gson.toJson(acc) : gson.toJson(Map.of("error", "Account not found"));
//                sendResponse(exchange, response);
//            }
//        }
//    }
//
//    static class DepositHandler implements HttpHandler {
//        public void handle(HttpExchange exchange) throws IOException {
//            if ("OPTIONS".equals(exchange.getRequestMethod())) {
//                sendCorsHeaders(exchange);
//                exchange.sendResponseHeaders(204, -1);
//                return;
//            }
//
//            if ("POST".equals(exchange.getRequestMethod())) {
//                String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
//                Map<String, Object> json = gson.fromJson(body, Map.class);
//
//                String id = (String) json.get("id");
//                double amount = ((Number) json.get("amount")).doubleValue();
//
//                Account acc = AccountDatabase.getAccount(id);
//                String response;
//
//                if (acc != null) {
//                    acc.deposit(amount);
//                    response = gson.toJson(Map.of("status", "Amount deposited", "balance", acc.getBalance()));
//                } else {
//                    response = gson.toJson(Map.of("error", "Account not found"));
//                }
//                sendResponse(exchange, response);
//            }
//        }
//    }
//
//    static class WithdrawHandler implements HttpHandler {
//        public void handle(HttpExchange exchange) throws IOException {
//            if ("OPTIONS".equals(exchange.getRequestMethod())) {
//                sendCorsHeaders(exchange);
//                exchange.sendResponseHeaders(204, -1);
//                return;
//            }
//
//            if ("POST".equals(exchange.getRequestMethod())) {
//                String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
//                Map<String, Object> json = gson.fromJson(body, Map.class);
//
//                String id = (String) json.get("id");
//                double amount = ((Number) json.get("amount")).doubleValue();
//
//                Account acc = AccountDatabase.getAccount(id);
//                String response;
//
//                if (acc != null && acc.withdraw(amount)) {
//                    response = gson.toJson(Map.of("status", "Amount withdrawn", "balance", acc.getBalance()));
//                } else {
//                    response = gson.toJson(Map.of("error", "Insufficient funds or account not found"));
//                }
//                sendResponse(exchange, response);
//            }
//        }
//    }
//
//    static class BalanceHandler implements HttpHandler {
//        public void handle(HttpExchange exchange) throws IOException {
//            if ("OPTIONS".equals(exchange.getRequestMethod())) {
//                sendCorsHeaders(exchange);
//                exchange.sendResponseHeaders(204, -1);
//                return;
//            }
//
//            if ("GET".equals(exchange.getRequestMethod())) {
//                String query = exchange.getRequestURI().getQuery();
//                String id = query.split("=")[1];
//
//                Account acc = AccountDatabase.getAccount(id);
//                String response = (acc != null) ?
//                        gson.toJson(Map.of("balance", acc.getBalance())) :
//                        gson.toJson(Map.of("error", "Account not found"));
//
//                sendResponse(exchange, response);
//            }
//        }
//    }
//
//    // Handle user registration
//    static class RegisterHandler implements HttpHandler {
//       public void handle(HttpExchange exchange) throws IOException {
//          if ("POST".equals(exchange.getRequestMethod())) {
//             String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
//             Map<String, String> json = gson.fromJson(body, Map.class);
//
//             boolean success = UserDatabase.register(json.get("username"),json.get("passwoed"));
//          }
//       }
//    }
//
//    // ---------- Utility ----------
//    private static void sendResponse(HttpExchange exchange, String response) throws IOException {
//        sendCorsHeaders(exchange);
//        exchange.sendResponseHeaders(200, response.getBytes().length);
//
//        OutputStream os = exchange.getResponseBody();
//        os.write(response.getBytes());
//        os.close();
//    }
//
//    private static void sendCorsHeaders(HttpExchange exchange) {
//        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "http://localhost:3000");
//        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
//        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
//    }
//}

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Main {
    private static final Gson gson = new Gson();

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8880), 0);

        // ---- Account endpoints ----
        server.createContext("/create", new CreateHandler());
        server.createContext("/view", new ViewHandler());
        server.createContext("/deposit", new DepositHandler());
        server.createContext("/withdraw", new WithdrawHandler());
        server.createContext("/balance", new BalanceHandler());

        // ---- User endpoints ----
        server.createContext("/register", new RegisterHandler());
        server.createContext("/login", new LoginHandler());

        server.setExecutor(null);
        System.out.println("✅ Server running on http://localhost:8880/");
        server.start();
    }

    // -------------------- Account Handlers --------------------

    static class CreateHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);

            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                Map<String, Object> json = gson.fromJson(body, Map.class);

                String id = (String) json.get("id");
                String name = (String) json.get("name");
                double balance = Double.parseDouble(json.get("balance").toString());

                Account acc = AccountDatabase.createAccount(id, name, balance);
                sendJsonResponse(exchange, gson.toJson(acc));
            } else {
                sendResponse(exchange, "Only POST allowed");
            }
        }
    }

    static class ViewHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);

            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                String query = exchange.getRequestURI().getQuery();
                String id = query != null && query.contains("id=") ? query.split("=")[1] : "";

                Account acc = AccountDatabase.getAccount(id);
                if (acc != null) {
                    sendJsonResponse(exchange, gson.toJson(acc));
                } else {
                    sendResponse(exchange, "Account not found");
                }
            } else {
                sendResponse(exchange, "Only GET allowed");
            }
        }
    }

    static class DepositHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);

            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                Map<String, Object> json = gson.fromJson(body, Map.class);

                String id = (String) json.get("id");
                double amount = Double.parseDouble(json.get("amount").toString());

                Account acc = AccountDatabase.deposit(id, amount);
                if (acc != null) {
                    sendJsonResponse(exchange, "Deposit Successful!");
                } else {
                    sendResponse(exchange, "Account not found");
                }
            } else {
                sendResponse(exchange, "Only POST allowed");
            }
        }
    }

    static class WithdrawHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);

            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                Map<String, Object> json = gson.fromJson(body, Map.class);

                String id = (String) json.get("id");
                double amount = Double.parseDouble(json.get("amount").toString());

                Account acc = AccountDatabase.withdraw(id, amount);
                if (acc != null) {
                    sendJsonResponse(exchange, "Withdraw Successful!");
                } else {
                    sendResponse(exchange, "Account not found or insufficient balance");
                }
            } else {
                sendResponse(exchange, "Only POST allowed");
            }
        }
    }

    static class BalanceHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);

            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                String query = exchange.getRequestURI().getQuery();
                String id = query != null && query.contains("id=") ? query.split("=")[1] : "";

                Account acc = AccountDatabase.getAccount(id);
                if (acc != null) {
                    Map<String, Object> result = new HashMap<>();
                    result.put("balance", acc.getBalance());
                    sendJsonResponse(exchange, gson.toJson(result));
                } else {
                    sendResponse(exchange, "Account not found");
                }
            } else {
                sendResponse(exchange, "Only GET allowed");
            }
        }
    }

    // -------------------- User Handlers --------------------

    static class RegisterHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);

            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                Map<String, String> json = gson.fromJson(body, Map.class);

                boolean success = UserDatabase.register(json.get("username"), json.get("password"));
                String response = success ? "User registered" : "User already exists";
                sendResponse(exchange, response);
            } else {
                sendResponse(exchange, "Only POST allowed");
            }
        }
    }

    static class LoginHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);

            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                Map<String, String> json = gson.fromJson(body, Map.class);

                boolean success = UserDatabase.login(json.get("username"), json.get("password"));
                String response = success ? "Login successful" : "Invalid username or password";
                sendResponse(exchange, response);
            } else {
                sendResponse(exchange, "Only POST allowed");
            }
        }
    }

    // -------------------- Utilities --------------------

    private static void sendResponse(HttpExchange exchange, String response) throws IOException {
        exchange.sendResponseHeaders(200, response.getBytes().length);
        exchange.getResponseBody().write(response.getBytes());
        exchange.getResponseBody().close();
    }

    private static void sendJsonResponse(HttpExchange exchange, String json) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, json.getBytes().length);
        exchange.getResponseBody().write(json.getBytes());
        exchange.getResponseBody().close();
    }

    // Add CORS headers
    private static void addCorsHeaders(HttpExchange exchange) {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
    }
}