<template>
    <div :class="active ? 'login_page_normal' : 'login_page_dark'" >
      <div id="particles-js"></div>
      <div type="button" class="button button5 absolute-top-right" @click="mudarLayout">
        <font-awesome-icon :icon="active ? 'moon' : 'sun'" />
      </div>
      <h2 class="text-center"><font-awesome-icon icon="gear" /> Blog <span>Admin Panel</span></h2>
      <form class="form-signin">
        <h4 class="h4 mb-4 text-center">
          <font-awesome-icon class="mr-1" icon="lock" />
          Login
        </h4>

        <label for="inputEmail" class="sr-only">Email address</label>
        <input type="email" v-model="email" class="form-control mb-3" placeholder="Email address" required autofocus>

        <label class="sr-only">Password</label>
        <input type="password" v-model="password" class="form-control mb-3" placeholder="Password" required>

        <div class="checkbox mb-2">
          <label style="font-size:0.9em">
            <input type="checkbox" style="vertical-align: text-top" v-model="remember"> Remember me
          </label>
        </div>

        <button class="btn btn-primary btn-block" type="submit" @click.prevent="login">Sign in</button>

        <hr />

        <div style="font-size:0.9em" class="text-center mt-1 text-secondary">
          Forgot your password?
          <router-link  class="" to="/register">
            Click here
          </router-link>
        </div>
      </form>

    </div>
</template>

<script type="text/javascript"></script>

<script>
export default {
  data: function () {
    return {
      email: '',
      password: '',
      remember: false,
      active:sessionStorage.getItem('@blog:active')
    }
  },

  methods: {

    mudarLayout() {

      if(this.active == true) {
        this.active = false
      } else {
        this.active = true
      }

      sessionStorage.setItem('@blog:active', this.active)
        
    },

    login() {
      
      
      let redirect = this.$auth.redirect();
      
      
      this.$auth.login({
        url: '/auth/login',
        redirect: {path: redirect ? redirect.from.path : '/dashboard'},
        data: {
          email: this.email,
          password: this.password
        },
        rememberMe: this.remember,
        success: (response) => {
          sessionStorage.setItem('@blog:user', JSON.stringify(response.data.user))
          this.$auth.user(response.data.user);
          this.$auth.token('access_token', response.data.token);
        },
        error: (error) => {
          window.console.log(error);
          window.console.log(error.data);
        },
        fetchUser: false
      });
    },
  },

  mounted() {
      particlesJS("particles-js", {
          "particles": {
              "number": {
                  "value": 80,
                  "density": {
                      "enable": true,
                      "value_area": 800
                  }
              },
              "color": {
                  "value": "#ffffff"
              },
              "shape": {
                  "type": "circle",
                  "stroke": {
                      "width": 0,
                      "color": "#000000"
                  },
                  "polygon": {
                      "nb_sides": 5
                  },
                  "image": {
                      "src": "img/github.svg",
                      "width": 100,
                      "height": 100
                  }
              },
              "opacity": {
                  "value": 0.5,
                  "random": false,
                  "anim": {
                      "enable": false,
                      "speed": 1,
                      "opacity_min": 0.1,
                      "sync": false
                  }
              },
              "size": {
                  "value": 3,
                  "random": true,
                  "anim": {
                      "enable": false,
                      "speed": 40,
                      "size_min": 0.1,
                      "sync": false
                  }
              },
              "line_linked": {
                  "enable": true,
                  "distance": 150,
                  "color": "#ffffff",
                  "opacity": 0.4,
                  "width": 1
              },
              "move": {
                  "enable": true,
                  "speed": 6,
                  "direction": "none",
                  "random": false,
                  "straight": false,
                  "out_mode": "out",
                  "bounce": false,
                  "attract": {
                      "enable": false,
                      "rotateX": 600,
                      "rotateY": 1200
                  }
              }
          },
          "interactivity": {
              "detect_on": "canvas",
              "events": {
                  "onhover": {
                      "enable": true,
                      "mode": "grab"
                  },
                  "onclick": {
                      "enable": true,
                      "mode": "push"
                  },
                  "resize": true
              },
              "modes": {
                  "grab": {
                      "distance": 140,
                      "line_linked": {
                          "opacity": 1
                      }
                  },
                  "bubble": {
                      "distance": 400,
                      "size": 40,
                      "duration": 2,
                      "opacity": 8,
                      "speed": 3
                  },
                  "repulse": {
                      "distance": 200,
                      "duration": 0.4
                  },
                  "push": {
                      "particles_nb": 4
                  },
                  "remove": {
                      "particles_nb": 2
                  }
              }
          },
          "retina_detect": true
      });
  },
  created(){
    this.active = sessionStorage.getItem('@blog:active')
  }
}
</script>
