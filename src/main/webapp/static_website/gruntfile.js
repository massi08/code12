'use strict';

module.exports = function (grunt) {
    grunt.initConfig({
        // Watch task config
        watch: {
            sass: {
                files: "./scss/*.scss",
                tasks: ['sass']
            }
        },
        // SASS task config
        sass: {
            dev: {
                files: {
                    // destination         // source file
                    "./css/style.css": "./scss/style.scss",
                    "./css/inscription.css": "./scss/inscription.scss",
                    "./css/connexion.css": "./scss/connexion.scss",
                    "./css/code.css": "./scss/code.scss",
                    "./css/create_project.css": "./scss/create_project.scss",
                    "./css/manage_project.css": "./scss/manage_project.scss",
                    "./css/manage_users.css": "./scss/manage_users.scss",
                    "./css/manage_tickets.css": "./scss/manage_tickets.scss",
                    "./css/manage_ticket.css": "./scss/manage_ticket.scss",
                    "./css/create_ticket.css": "./scss/create_ticket.scss",
                    "./css/parameters.css": "./scss/parameters.scss",
                    "./css/account.css": "./scss/account.scss",
                    "./css/custom_jstree.css": "./scss/custom_jstree.scss"
                }
            }
        },

        browserSync: {
            default_options: {
                bsFiles: {
                    src: [
                        "css/*.css",
                        "*.html",
                        "js/**",
                        "../WEB-INF/pages/**"
                    ]
                },
                options: {
                    port: "1234",
                    watchTask: true,
                    proxy: {
                        target: "localhost:8080",
                        ws: true // enables websockets
                    },
                    ui: {
                        port: "1235"
                    }
                }
            }
        }
    });

    grunt.loadNpmTasks('grunt-sass');
    grunt.loadNpmTasks('grunt-contrib-watch');
    grunt.loadNpmTasks('grunt-browser-sync');
    grunt.registerTask('default', ['browserSync', 'watch']);
};