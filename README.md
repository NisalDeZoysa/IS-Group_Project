# Secure Voting System - Client-Server Implementation

A Java-based secure electronic voting system that implements RSA encryption and digital signatures to ensure vote integrity and authenticity.

## 🚀 Features

- **Secure Communication**: RSA encryption for vote confidentiality
- **Digital Signatures**: SHA256withRSA signatures for vote authenticity
- **Multi-Voter Support**: Handles up to 10 votes per session
- **Real-time Results**: Live vote counting and final results display
- **Tamper Detection**: Signature verification prevents vote manipulation

## 📋 System Requirements

- Java 8 or higher
- Network connectivity (localhost)
- Console/Terminal access

## 📁 Project Structure

```
VotingSystemClientServer/
├── src/
│   └── ServerClient/
│       ├── Client.java          # Voting client application
│       ├── Server.java          # Vote counting server
│       ├── KeyUtils.java        # RSA key management utilities
│       └── KeyGeneratorUtil.java # Key pair generation
├── bin/
│   ├── ServerClient/            # Compiled class files
│   ├── client_private.key       # Client's private key
│   ├── client_public.key        # Client's public key
│   ├── server_private.key       # Server's private key
│   └── server_public.key        # Server's public key
└── README.md
```

## 🔧 Setup and Installation

### 1. Clone the Repository
```bash
git clone <repository-url>
cd VotingSystemClientServer
```

### 2. Generate RSA Key Pairs (if not present)
```bash
cd bin
java ServerClient.KeyGeneratorUtil
```
This creates the required key files:
- `client_private.key` / `client_public.key`
- `server_private.key` / `server_public.key`

### 3. Compile the Source Code
```bash
# From the project root directory
javac -d bin src/ServerClient/*.java
```

## 🚀 Running the System

### Step 1: Start the Server
Open a terminal and navigate to the bin directory:
```bash
cd bin
java ServerClient.Server
```

Expected output:
```
Counting Center Server
========================
Waiting for 10 votes...
```

### Step 2: Run the Client
Open another terminal and navigate to the bin directory:
```bash
cd bin
java ServerClient.Client
```

### Step 3: Cast Votes
The client will prompt for 10 votes sequentially:
```
--- Voter 1 ---
Waiting for vote input...
Enter your vote (1, 2, 3, or 4): 2
Vote received: 2
Connecting to server to send vote #1...
Vote 1 sent successfully.
```

## 🗳️ Voting Process

1. **Vote Input**: Each voter enters a choice (1, 2, 3, or 4)
2. **Encryption**: Vote is encrypted using server's public key
3. **Digital Signing**: Encrypted vote is signed with client's private key
4. **Secure Transmission**: Encrypted and signed vote sent to server
5. **Verification**: Server verifies signature using client's public key
6. **Decryption**: Server decrypts vote using its private key
7. **Counting**: Valid votes are added to the tally

## 📊 Results Display

After collecting all votes, the server displays:

```
========================================
         FINAL ELECTION RESULTS
========================================
Candidate 1: 3 vote(s)
Candidate 2: 4 vote(s)
Candidate 3: 2 vote(s)
Candidate 4: 1 vote(s)
----------------------------------------
Total votes counted: 10

🏆 WINNER: Candidate 2 with 4 vote(s)!

Voting session completed. Server shutting down.
```

## 🔒 Security Features

### Encryption
- **Algorithm**: RSA with 2048-bit keys
- **Purpose**: Ensures vote confidentiality during transmission
- **Implementation**: Each vote encrypted with server's public key

### Digital Signatures
- **Algorithm**: SHA256withRSA
- **Purpose**: Guarantees vote authenticity and integrity
- **Process**: Client signs encrypted vote with private key

### Key Management
- **Key Generation**: Automated RSA key pair generation
- **Key Storage**: Separate key files for client and server
- **Key Usage**: Public keys for encryption/verification, private keys for decryption/signing

## ⚙️ Configuration

### Modifying Vote Limits
To change the number of votes collected, modify the `MAX_VOTES` constant in `Server.java`:
```java
static final int MAX_VOTES = 10; // Change this value
```

### Changing Network Port
To use a different port, modify the `port` variable in both files:
```java
static int port = 6600; // Change this value
```

### Adding More Candidates
To support more than 4 candidates, update the validation regex in `Client.java`:
```java
if (vote.matches("[1-6]")) { // For 6 candidates
```

## 🛠️ Troubleshooting

### Common Issues

**1. "Connection refused" Error**
- Ensure the server is running before starting the client
- Check if port 6600 is available

**2. "Key file not found" Error**
- Run `KeyGeneratorUtil` to generate key files
- Ensure you're running from the `bin` directory

**3. "Signature verification failed"**
- Verify key files are not corrupted
- Ensure client and server are using matching key pairs

**4. Client terminates after one vote**
- Recompile the source code: `javac -d bin src/ServerClient/*.java`
- Make sure you're running the latest compiled version

### Debugging Steps

1. **Check file locations**:
   ```bash
   ls -la bin/
   # Should show all .key files
   ```

2. **Verify compilation**:
   ```bash
   javac -d bin src/ServerClient/*.java
   ```

3. **Test key generation**:
   ```bash
   java ServerClient.KeyGeneratorUtil
   ```

## 🏗️ Architecture Overview

```
┌─────────────┐                    ┌─────────────┐
│   Client    │                    │   Server    │
│             │                    │             │
│ 1. Get Vote │                    │ 6. Verify   │
│ 2. Encrypt  │ ──── Network ───→  │ 7. Decrypt  │
│ 3. Sign     │    (Secure)        │ 8. Count    │
│ 4. Send     │                    │ 9. Results  │
└─────────────┘                    └─────────────┘
```

## 📝 System Limitations

- **Single Session**: Server processes one voting session then terminates
- **Local Network**: Currently configured for localhost communication
- **Sequential Voting**: One client processes all votes sequentially
- **Fixed Candidates**: Supports 4 candidates (1, 2, 3, 4)

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/new-feature`)
3. Commit changes (`git commit -am 'Add new feature'`)
4. Push to branch (`git push origin feature/new-feature`)
5. Create a Pull Request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 👥 Authors

- Your Name - Initial work

## 🙏 Acknowledgments

- RSA cryptography implementation using Java Cryptography Architecture (JCA)
- Network communication using Java Socket API
