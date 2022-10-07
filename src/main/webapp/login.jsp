<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <link href="static/css/style-login.css" rel="stylesheet"/>
    <link
            href="https://unpkg.com/boxicons@2.1.2/css/boxicons.min.css"
            rel="stylesheet"
    />
    <title>Cargo-Delivery | Login</title>
</head>
<body>

<section class="container">
    <div class="login">
        <header>Login</header>
        <form method="post" action="login">
            <div class="field login-field">
                <div class="input-field">
                    <input type="text" name="login" id="login" placeholder="Login" class="login"/>
                </div>
                <span class="error login-error">
                    <i class="bx bx-error-circle error-icon"></i>
                    <p class="error-text">Provide a valid login!</p>
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

            <div class="input-field btn-field">
                <button type="submit" name="login_btn" id="login_btn">Login</button>
            </div>
        </form>

        <div class="link-field">
            <span>Don't have an account? <a href="signup.jsp" class="link signup-link">Signup</a></span>
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
<script src="static/js/script-login.js"></script>

</body>
</html>
