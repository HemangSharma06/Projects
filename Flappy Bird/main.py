import pygame
import random
import os
import sys
from pygame import MOUSEBUTTONDOWN, MOUSEBUTTONUP, FINGERDOWN, FINGERUP, KEYDOWN, K_SPACE

# Initialize pygame
pygame.init()

# Game constants
SCREEN_WIDTH = 360
SCREEN_HEIGHT = 640
GRAVITY = 0.25
FLAP_STRENGTH = -7
PIPE_SPEED = 3
PIPE_GAP = 150
PIPE_FREQUENCY = 1500  # milliseconds

# Colors
WHITE = (255, 255, 255)
BLACK = (0, 0, 0)
GREEN = (0, 128, 0)
SKY_BLUE = (135, 206, 235)

# Set up the display
screen = pygame.display.set_mode((SCREEN_WIDTH, SCREEN_HEIGHT))
pygame.display.set_caption("Flappy Bird")

# Clock for controlling frame rate
clock = pygame.time.Clock()
FPS = 60

# Font for score display
font = pygame.font.SysFont('Arial', 30)


class Bird:
    def __init__(self):
        self.x = 100
        self.y = SCREEN_HEIGHT // 2
        self.velocity = 0
        self.width = 40
        self.height = 30
        self.color = (255, 255, 0)  # Yellow

    def update(self):
        # Apply gravity
        self.velocity += GRAVITY
        self.y += self.velocity

        # Keep bird within screen bounds
        if self.y < 0:
            self.y = 0
            self.velocity = 0
        if self.y > SCREEN_HEIGHT - self.height:
            self.y = SCREEN_HEIGHT - self.height
            self.velocity = 0

    def flap(self):
        self.velocity = FLAP_STRENGTH

    def draw(self):
        pygame.draw.rect(screen, self.color, (self.x, self.y, self.width, self.height))
        # Draw eye
        pygame.draw.circle(screen, BLACK, (self.x + 30, self.y + 10), 5)
        # Draw beak
        pygame.draw.polygon(screen, (255, 165, 0), [(self.x + 40, self.y + 15),
                                                    (self.x + 50, self.y + 15),
                                                    (self.x + 40, self.y + 20)])

    def get_mask(self):
        return pygame.Rect(self.x, self.y, self.width, self.height)


class Pipe:
    def __init__(self):
        self.x = SCREEN_WIDTH
        self.width = 60
        self.top_height = random.randint(50, SCREEN_HEIGHT - PIPE_GAP - 50)
        self.bottom_height = SCREEN_HEIGHT - self.top_height - PIPE_GAP
        self.color = GREEN
        self.passed = False

    def update(self):
        self.x -= PIPE_SPEED

    def draw(self):
        # Top pipe
        pygame.draw.rect(screen, self.color, (self.x, 0, self.width, self.top_height))
        # Bottom pipe
        pygame.draw.rect(screen, self.color,
                         (self.x, SCREEN_HEIGHT - self.bottom_height, self.width, self.bottom_height))

    def collide(self, bird):
        bird_rect = bird.get_mask()
        top_pipe = pygame.Rect(self.x, 0, self.width, self.top_height)
        bottom_pipe = pygame.Rect(self.x, SCREEN_HEIGHT - self.bottom_height, self.width, self.bottom_height)

        return bird_rect.colliderect(top_pipe) or bird_rect.colliderect(bottom_pipe)


def load_high_score():
    try:
        with open('highscore.txt', 'r') as file:
            return int(file.read())
    except (FileNotFoundError, ValueError):
        return 0


def save_high_score(score):
    with open('highscore.txt', 'w') as file:
        file.write(str(score))


def draw_score(score, high_score):
    score_text = font.render(f"Score: {score}", True, WHITE)
    high_score_text = font.render(f"High Score: {high_score}", True, WHITE)
    screen.blit(score_text, (10, 10))
    screen.blit(high_score_text, (10, 50))


def game_over_screen(score, high_score):
    screen.fill(SKY_BLUE)
    game_over_text = font.render("Game Over", True, BLACK)
    score_text = font.render(f"Your Score: {score}", True, BLACK)
    high_score_text = font.render(f"High Score: {high_score}", True, BLACK)
    restart_text = font.render("Tap to Restart", True, BLACK)

    screen.blit(game_over_text, (SCREEN_WIDTH // 2 - game_over_text.get_width() // 2, 150))
    screen.blit(score_text, (SCREEN_WIDTH // 2 - score_text.get_width() // 2, 200))
    screen.blit(high_score_text, (SCREEN_WIDTH // 2 - high_score_text.get_width() // 2, 250))
    screen.blit(restart_text, (SCREEN_WIDTH // 2 - restart_text.get_width() // 2, 350))

    pygame.display.update()

    waiting = True
    while waiting:
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                pygame.quit()
                sys.exit()
            if event.type == MOUSEBUTTONDOWN or event.type == FINGERDOWN or (
                    event.type == KEYDOWN and event.key == K_SPACE):
                waiting = False


def main():
    bird = Bird()
    pipes = []
    score = 0
    high_score = load_high_score()

    last_pipe = pygame.time.get_ticks()
    running = True
    game_active = True

    while running:
        clock.tick(FPS)

        # Event handling
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                running = False

            # Handle all possible input methods
            if event.type == MOUSEBUTTONDOWN or event.type == FINGERDOWN or (
                    event.type == KEYDOWN and event.key == K_SPACE):
                if game_active:
                    bird.flap()
                else:
                    # Restart game
                    bird = Bird()
                    pipes = []
                    score = 0
                    last_pipe = pygame.time.get_ticks()
                    game_active = True

        # Clear screen
        screen.fill(SKY_BLUE)

        if game_active:
            # Bird update
            bird.update()

            # Generate pipes
            time_now = pygame.time.get_ticks()
            if time_now - last_pipe > PIPE_FREQUENCY:
                pipes.append(Pipe())
                last_pipe = time_now

            # Pipe update and collision detection
            for pipe in pipes[:]:
                pipe.update()

                # Check if bird passed the pipe
                if pipe.x + pipe.width < bird.x and not pipe.passed:
                    pipe.passed = True
                    score += 1
                    if score > high_score:
                        high_score = score
                        save_high_score(high_score)

                # Check collision
                if pipe.collide(bird):
                    game_active = False

                # Remove pipes that are off screen
                if pipe.x < -pipe.width:
                    pipes.remove(pipe)

            # Check if bird hit the ground or ceiling
            if bird.y + bird.height >= SCREEN_HEIGHT or bird.y <= 0:
                game_active = False

        # Drawing
        for pipe in pipes:
            pipe.draw()

        bird.draw()
        draw_score(score, high_score)

        if not game_active:
            game_over_screen(score, high_score)

        pygame.display.update()

    pygame.quit()
    sys.exit()


if __name__ == "__main__":
    main()