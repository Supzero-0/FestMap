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
import { Festival, FestivalRequest } from '../../../festivals/models/festival-model';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { TextareaModule } from 'primeng/textarea';
import { DatePickerModule } from 'primeng/datepicker';
import { DividerModule } from 'primeng/divider';

@Component({
  selector: 'app-festival-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    ButtonModule,
    InputTextModule,
    TextareaModule,
    DatePickerModule,
    DividerModule,
  ],
  templateUrl: './festival-form.html',
  styleUrl: './festival-form.scss',
})
export class FestivalForm implements OnInit, OnChanges {
  private readonly fb = inject(FormBuilder);

  @Input() festival: Festival | null = null;
  @Input() readOnly = false;
  @Output() save = new EventEmitter<FestivalRequest>();
  @Output() cancel = new EventEmitter<void>();

  festivalForm: FormGroup = this.initForm();

  ngOnInit(): void {
    if (this.festival) {
      this.patchForm(this.festival);
    }
    this.updateFormState();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['festival'] && this.festival) {
      this.patchForm(this.festival);
    } else if (changes['festival'] && !this.festival) {
      this.festivalForm.reset();
    }

    if (changes['readOnly']) {
      this.updateFormState();
    }
  }

  private updateFormState(): void {
    if (this.readOnly) {
      this.festivalForm.disable();
    } else {
      this.festivalForm.enable();
    }
  }

  private initForm(): FormGroup {
    return this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3)]],
      genre: [''],
      description: [''],
      startDate: [null, Validators.required],
      endDate: [null, Validators.required],
      address: this.fb.group({
        addressLine: ['', Validators.required],
        city: ['', Validators.required],
        postalCode: ['', Validators.required],
        country: ['', Validators.required],
        latitude: [null, [Validators.required, Validators.min(-90), Validators.max(90)]],
        longitude: [null, [Validators.required, Validators.min(-180), Validators.max(180)]],
      }),
    });
  }

  private patchForm(festival: Festival): void {
    this.festivalForm.patchValue({
      name: festival.name,
      genre: festival.genre,
      description: festival.description,
      startDate: new Date(festival.startDate),
      endDate: new Date(festival.endDate),
      address: {
        addressLine: festival.address.addressLine,
        city: festival.address.city,
        postalCode: festival.address.postalCode,
        country: festival.address.country,
        latitude: festival.address.latitude,
        longitude: festival.address.longitude,
      },
    });
  }

  onSubmit(): void {
    if (this.festivalForm.valid && !this.readOnly) {
      const formValue = this.festivalForm.getRawValue();

      // Formater les dates en YYYY-MM-DD pour le backend (LocalDate)
      const formatDate = (date: Date) => {
        if (!date) return null;
        const d = new Date(date);
        return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`;
      };

      const festivalRequest: FestivalRequest = {
        ...formValue,
        startDate: formatDate(formValue.startDate)!,
        endDate: formatDate(formValue.endDate)!,
      };

      this.save.emit(festivalRequest);
    } else if (!this.readOnly) {
      this.festivalForm.markAllAsTouched();
    }
  }

  onCancel(): void {
    this.cancel.emit();
  }
}
