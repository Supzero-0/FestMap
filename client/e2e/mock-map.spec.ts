import test, { expect } from "@playwright/test";
import { readFileSync } from "fs";
import { dirname, resolve } from "path";
import { fileURLToPath } from "url";

const __filename = fileURLToPath(import.meta.url);
const __dirname = dirname(__filename);

function readMockFestivalCount() {
    const jsonPath = resolve(__dirname, '../src/features/festivals/data/mockFestivals.json');
    const content = readFileSync(jsonPath, 'utf-8');
    const arr = JSON.parse(content);
    return Array.isArray(arr) ? arr.length : 0;
}

test.describe('Mock festival map minimal flow', () => {
    // Vérification de l'affichage de la carte
    test('Carte visible', async ({ page }) => {
        await page.goto('/');
        await expect(page.getByTestId('festival-map')).toBeVisible();
    });

    // Vérification que le nombre de marqueurs soit égal au mock JSON
    test(`Nombre de marqueurs = nombre d'itemsJSON`, async ({ page }) => {
        await page.goto('/');

        // Récupération du nombre de festival dans le mock
        const jsonCount = readMockFestivalCount();
        // Récupération des data-testid
        const markers = page.locator('[data-testid^="festival-marker-"]')

        // Résultat attendu
        await expect(markers).toHaveCount(jsonCount);
    });

    // Vérification de l'affichage de la popup
    test('Click sur item liste -> popup visible', async ({ page }) => {
        await page.goto('/');

        // Récupération des data-testid
        const firstItem = page.locator('[data-testid^="festival-list-item-"]').first();

        // Action
        await firstItem.click();

        // Résultat attendu
        await expect(page.getByTestId('festival-popup-1')).toBeVisible();
    });

    // Vérification du fonctionnement de l'action delete
    test(`Action secondaire (delete) modifie l'UI`, async ({ page }) => {
        await page.goto('/');

        // Récupération des data-testid
        const items = page.locator('[data-testid^="festival-list-item-"]');
        const itemsBefore = await items.count();
        const markers = page.locator('[data-testid^="festival-marker-"]')
        const markersBefore = await items.count();

        // Action
        await page.getByTestId(`delete-festival-list-item-1`).click();

        // Résultats attendus
        await expect(items).toHaveCount(itemsBefore - 1);
        await expect(markers).toHaveCount(markersBefore - 1); 
    })
})