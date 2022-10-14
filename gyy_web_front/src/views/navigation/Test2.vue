<template>
    <div>
        <form action="http://localhost/login" method="post">
        用户名  <input type="text" name="username">
          密码  <input type="password" name="password">
              <input type="submit" >
        </form>
    </div>
</template>

<script>
export default {
  name: 'Test2'
}
</script>
