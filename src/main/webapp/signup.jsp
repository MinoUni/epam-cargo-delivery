<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <link href="static/css/style-signup.css" rel="stylesheet"/>
    <link
            href="https://unpkg.com/boxicons@2.1.2/css/boxicons.min.css"
            rel="stylesheet"
    />
    <title>Cargo-Delivery | Signup</title>
</head>
<body>
<section class="container">
    <div class="signup">
        <header>Signup</header>
        <form method="post" action="signup">
            <div class="field login-field">
                <div class="input-field">
                    <input type="text" name="login" id="login" placeholder="Login" class="login"/>
                </div>
                <span class="error login-error">
                    <i class="bx bx-error-circle error-icon"></i>
                    <p class="error-text">Provide a valid login!</p>
                </span>
            </div>

            <div class="field name-field">
                <div class="input-field">
                    <input type="text" name="name" id="name" placeholder="Name" class="name"/>
                </div>
                <span class="error name-error">
                    <i class="bx bx-error-circle error-icon"></i>
                    <p class="error-text">Provide a valid name!</p>
                </span>
            </div>

            <div class="field surname-field">
                <div class="input-field">
                    <input type="text" name="surname" id="surname" placeholder="Surname" class="surname"/>
                </div>
                <span class="error surname-error">
                    <i class="bx bx-error-circle error-icon"></i>
                    <p class="error-text">Provide a valid surname!</p>
                </span>
            </div>

            <div class="field email-field">
                <div class="input-field">
                    <input type="email" name="email" id="email" placeholder="Email" class="email"/>
                </div>
                <span class="error email-error">
                    <i class="bx bx-error-circle error-icon"></i>
                    <p class="error-text">Provide a valid email!</p>
                </span>
            </div>

            <div class="field password-field">
                <div class="input-field">
                    <input type="password"
                           name="password"
                           id="password"
                           placeholder="Password"
                           class="password"/>
                    <i class="bx bx-hide show-pass"></i>
                </div>
                <span class="error password-error">
                        <i class="bx bx-error-circle error-icon"></i>
                        <p class="error-text">Provide a valid password!</p>
                    </span>
            </div>

            <div class="field confirm-password-field">
                <div class="input-field">
                    <input type="password"
                           name="confirm-password"
                           id="confirm-password"
                           placeholder="Confirm password"
                           class="confirm-password"/>
                </div>
                <span class="error confirm-password-error">
                        <i class="bx bx-error-circle error-icon"></i>
                        <p class="error-text">Password don't match!</p>
                    </span>
            </div>

            <div class="input-field btn-field">
                <button type="submit" name="signup_btn" id="signup_btn">Signup</button>
            </div>
        </form>

        <div class="link-field">
            <span>Already have an account? <a href="login.jsp" class="link login-link">Login</a></span>
        </div>

        <div class="line"></div>

        <div class="back-home">
            <a href="index.jsp" class="input-field home-link">
                <i class="bx bxs-box box icon"></i>
                <span>Back Index as Guest</span>
            </a>
        </div>
    </div>
</section>

<!-- JavaScript -->
<script src="static/js/script-signup.js"></script>
</body>
</html>
