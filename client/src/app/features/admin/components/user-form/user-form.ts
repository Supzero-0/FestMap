import {
  Component,
  EventEmitter,
  Input,
  OnInit,
  Output,
  OnChanges,
  SimpleChanges,
  inject,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { User, Role } from '../../../auth/models/user-model';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { SelectModule } from 'primeng/select';

@Component({
  selector: 'app-user-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    ButtonModule,
    InputTextModule,
    PasswordModule,
    SelectModule,
  ],
  templateUrl: './user-form.html',
  styleUrl: './user-form.scss',
})
export class UserForm implements OnInit, OnChanges {
  private readonly fb = inject(FormBuilder);

  @Input() user: User | null = null;
  @Input() readOnly = false;
  @Output() save = new EventEmitter<User>();
  @Output() cancel = new EventEmitter<void>();

  userForm: FormGroup = this.initForm();

  roles = [
    { label: 'Utilisateur', value: 'USER' },
    { label: 'Administrateur', value: 'ADMIN' },
  ];

  ngOnInit(): void {
    if (this.user) {
      this.patchForm(this.user);
    }
    this.updateFormState();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['user'] && this.user) {
      this.patchForm(this.user);
    } else if (changes['user'] && !this.user) {
      this.userForm.reset({ role: 'USER' });
    }

    if (changes['readOnly']) {
      this.updateFormState();
    }
  }

  private updateFormState(): void {
    if (this.readOnly) {
      this.userForm.disable();
    } else {
      this.userForm.enable();
    }
  }

  private initForm(): FormGroup {
    return this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', this.user ? [] : [Validators.required, Validators.minLength(6)]],
      role: ['USER', Validators.required],
    });
  }

  private patchForm(user: User): void {
    this.userForm.patchValue({
      email: user.email,
      role: user.role,
      password: '',
    });
    this.userForm
      .get('password')
      ?.setValidators(
        user ? [Validators.minLength(6)] : [Validators.required, Validators.minLength(6)],
      );
    this.userForm.get('password')?.updateValueAndValidity();
  }

  onSubmit(): void {
    if (this.userForm.valid && !this.readOnly) {
      const formValue = this.userForm.getRawValue();
      const userResult: User = {
        ...formValue,
      };
      if (this.user && !userResult.password) {
        delete userResult.password;
      }
      this.save.emit(userResult);
    } else if (!this.readOnly) {
      this.userForm.markAllAsTouched();
    }
  }

  onCancel(): void {
    this.cancel.emit();
  }
}
