provider "aws" {
  region = "us-west-2"
}

resource "aws_vpc" "main_vpc" {
  cidr_block = "10.0.0.0/16"
}

resource "aws_subnet" "main_subnet" {
  vpc_id     = aws_vpc.main_vpc.id
  cidr_block = "10.0.1.0/24"
}

resource "aws_security_group" "ecs_security_group" {
  vpc_id = aws_vpc.main_vpc.id

  ingress {
    from_port   = 8080
    to_port     = 8080
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_ecs_cluster" "my_cluster" {
  name = "myapp-cluster"
}

resource "aws_ecr_repository" "myapp_repo" {
  name = "myapp-repo"
}

resource "aws_iam_role" "ecs_task_execution_role" {
  name = "ecsTaskExecutionRole"

  assume_role_policy = jsonencode({
    Version = "2012-10-17",
    Statement = [{
      Action    = "sts:AssumeRole"
      Effect    = "Allow"
      Principal = {
        Service = "ecs-tasks.amazonaws.com"
      }
    }]
  })

  managed_policy_arns = [
    "arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy"
  ]
}

resource "aws_ecs_task_definition" "myapp_task" {
  family                   = "myapp-task"
  requires_compatibilities = ["FARGATE"]
  network_mode             = "awsvpc"
  cpu                      = "256"
  memory                   = "512"

  container_definitions = jsonencode([{
    name      = "myapp"
    image     = aws_ecr_repository.myapp_repo.repository_url  # Cambia a tu imagen en ECR o Docker Hub
    essential = true
    portMappings = [{
      containerPort = 8080
      hostPort      = 8080
    }]
  }])

  execution_role_arn = aws_iam_role.ecs_task_execution_role.arn
}

resource "aws_ecs_service" "myapp_service" {
  name            = "myapp-service"
  cluster         = aws_ecs_cluster.my_cluster.id
  task_definition = aws_ecs_task_definition.myapp_task.arn
  desired_count   = 1
  launch_type     = "FARGATE"

  network_configuration {
    subnets         = [aws_subnet.main_subnet.id]
    security_groups = [aws_security_group.ecs_security_group.id]
  }
}

resource "aws_lb" "myapp_lb" {
  name               = "myapp-lb"
  internal           = false
  load_balancer_type = "application"
  security_groups    = [aws_security_group.ecs_security_group.id]
  subnets            = [aws_subnet.main_subnet.id]
}

resource "aws_lb_listener" "app_listener" {
  load_balancer_arn = aws_lb.myapp_lb.arn
  port              = 80
  protocol          = "HTTP"

  default_action {
    target_group_arn = aws_lb_target_group.myapp_tg.arn
    type             = "forward"
  }
}

resource "aws_lb_target_group" "myapp_tg" {
  name        = "myapp-tg"
  port        = 8080
  protocol    = "HTTP"
  vpc_id      = aws_vpc.main_vpc.id
  target_type = "ip"
}

resource "aws_lb_target_group_attachment" "myapp_tg_attachment" {
  target_group_arn = aws_lb_target_group.myapp_tg.arn
  target_id        = aws_ecs_service.myapp_service.id
  port             = 8080
}
